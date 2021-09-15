package de.dfs.graffitiboard.message.api

import de.dfs.graffitiboard.message.service.Message
import de.dfs.graffitiboard.message.service.MessageService
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import java.time.ZonedDateTime

internal class MessageControllerTest {
    companion object {
        val message1 = Message(author = null, message = "Message 1")
        val message2 = Message(author = "John Doe", message = "Message 2")

        val messageReadDto1 = MessageReadDto(author = null, message = "Message 1", createdAt = ZonedDateTime.now())
        val messageReadDto2 =
            MessageReadDto(author = "John Doe", message = "Message 2", createdAt = ZonedDateTime.now())
    }

    private val messageDtoMapper = mockk<MessageDtoMapper>()
    private val messageService = mockk<MessageService>()
    private val messageFlux = mockk<Flux<Message>>()

    private val sut = MessageController(messageDtoMapper, messageService, messageFlux)

    @BeforeEach
    internal fun setUp() {
        every { messageDtoMapper.mapToReadDto(message1) } returns messageReadDto1
        every { messageDtoMapper.mapToReadDto(message2) } returns messageReadDto2
    }

    @Test
    internal fun `getAll - when there are persisted messages it should return all messages`() {
        every { messageService.findAll() } returns listOf(message1, message2)

        assertThat(sut.getAll()).containsExactly(messageReadDto1, messageReadDto2)

        verify(exactly = 2) { messageDtoMapper.mapToReadDto(any()) }
    }

    @Test
    internal fun `getAll - when there no persisted messages it should return empty list`() {
        every { messageService.findAll() } returns emptyList()

        assertThat(sut.getAll()).isEmpty()

        verify { messageDtoMapper wasNot Called }
    }
}
