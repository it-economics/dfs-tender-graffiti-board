package de.dfs.graffitiboard.message.socket

import com.fasterxml.jackson.databind.ObjectMapper
import de.dfs.graffitiboard.message.api.MessageDto
import de.dfs.graffitiboard.message.api.MessageDtoMapper
import de.dfs.graffitiboard.message.api.MessageReadDto
import de.dfs.graffitiboard.message.service.Message
import de.dfs.graffitiboard.message.service.MessageService
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.web.socket.TextMessage
import java.time.ZonedDateTime
import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException
import javax.validation.Validator

internal class TextMessageProcessorTest {

    companion object {
        val message = Message(author = null, message = "Message")
        val messageReadDto = MessageReadDto(author = null, message = "Message 1", createdAt = ZonedDateTime.now())
    }

    private val validator = mockk<Validator>()
    private val objectMapper = mockk<ObjectMapper>()
    private val messageService = mockk<MessageService>()
    private val messageDtoMapper = mockk<MessageDtoMapper>()

    private val sut = TextMessageProcessor(validator, objectMapper, messageService, messageDtoMapper)

    @BeforeEach
    internal fun setUp() {
        every { messageDtoMapper.mapToReadDto(message) } returns messageReadDto
        every { objectMapper.writeValueAsString(any()) } returns "outgoing_message_json"
        every { validator.validate(any<MessageDto>()) } returns emptySet()
    }

    @Test
    internal fun processNewConnection() {
        every { messageService.findAll() } returns listOf(message)

        val result = sut.processNewConnection()

        assertThat(result).hasSize(1)

        verify { objectMapper.writeValueAsString(messageReadDto) }
    }

    @Test
    internal fun processNewMessage() {
        val textMessage = TextMessage("incoming_message_json")

        val messageDto = MessageDto(author = "John Doe", message = "Message 1")
        every { objectMapper.readValue(any<String>(), MessageDto::class.java) } returns messageDto
        every { messageDtoMapper.mapToMessage(messageDto) } returns message
        every { messageService.create(message) } returns message

        val result = sut.processNewMessage(textMessage)

        assertThat(result.payload).isEqualTo("outgoing_message_json")

        verify {
            validator.validate(messageDto)
            messageService.create(message)
            objectMapper.writeValueAsString(messageReadDto)
        }
    }

    @Test
    internal fun `processNewMessage - when message is invalid it should raise an exception`() {
        val textMessage = TextMessage("invalid_message_json")
        val messageDto = MessageDto(author = "John Doe", message = "")

        val constraintViolation = mockk<ConstraintViolation<MessageDto>>()
        every { constraintViolation.message } returns "Empty message not allowed"

        every { objectMapper.readValue(any<String>(), MessageDto::class.java) } returns messageDto
        every { validator.validate(messageDto) } returns setOf(constraintViolation)

        assertThatExceptionOfType(ConstraintViolationException::class.java)
            .isThrownBy { sut.processNewMessage(textMessage) }

        verify { messageService wasNot Called }
    }
}
