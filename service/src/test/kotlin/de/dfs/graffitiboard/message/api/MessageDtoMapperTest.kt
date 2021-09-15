package de.dfs.graffitiboard.message.api

import de.dfs.graffitiboard.message.service.Message
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime

internal class MessageDtoMapperTest {

    private val sut = MessageDtoMapper()

    @Test
    internal fun `mapToMessage - complete message dto`() {
        val messageDto = MessageDto(author = "John Doe", message = "Hello Wolrd")

        val message = sut.mapToMessage(messageDto)

        assertThat(message.author).isEqualTo(messageDto.author)
        assertThat(message.message).isEqualTo(messageDto.message)
        assertThat(message.createdAt).isNull()
    }

    @Test
    internal fun `mapToMessage - message dto without author`() {
        val messageDto = MessageDto(author = null, message = "Hello Wolrd")

        val message = sut.mapToMessage(messageDto)

        assertThat(message.author).isNull()
        assertThat(message.message).isEqualTo(messageDto.message)
        assertThat(message.createdAt).isNull()
    }

    @Test
    internal fun `mapToReadDto - complete message`() {
        val message = Message(author = "John Doe", message = "Hello World", createdAt = ZonedDateTime.now())

        val messageReadDto = sut.mapToReadDto(message)

        assertThat(messageReadDto.author).isEqualTo(message.author)
        assertThat(messageReadDto.message).isEqualTo(message.message)
        assertThat(messageReadDto.createdAt).isEqualTo(message.createdAt)
    }

    @Test
    internal fun `mapToReadDto - message without author`() {
        val message = Message(author = null, message = "Hello World", createdAt = ZonedDateTime.now())

        val messageReadDto = sut.mapToReadDto(message)

        assertThat(messageReadDto.author).isNull()
        assertThat(messageReadDto.message).isEqualTo(message.message)
        assertThat(messageReadDto.createdAt).isEqualTo(message.createdAt)
    }
}
