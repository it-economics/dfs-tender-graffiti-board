package de.dfs.graffitiboard.service

import de.dfs.graffitiboard.api.MessageDto
import de.dfs.graffitiboard.api.MessageReadDto
import de.dfs.graffitiboard.persistence.MessageEntity
import de.dfs.graffitiboard.persistence.MessageRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import reactor.core.publisher.Sinks

@Service
class MessageService(
    private val messageRepository: MessageRepository,
    private val messageSink: Sinks.Many<MessageReadDto>
) {
    companion object {
        private val log = KotlinLogging.logger { }
    }

    fun create(message: MessageDto): MessageReadDto =
        run { mapToEntity(message) }
            .let { messageRepository.save(it) }
            .let { mapToDto(it) }
            .also { publish(it) }

    fun findAll() = messageRepository.findAll().map { mapToDto(it) }

    fun publish(message: MessageReadDto) {
        runCatching { messageSink.tryEmitNext(message) }
            .onSuccess { log.info("Published new message") }
            .onFailure { e -> log.warn(e) { "Error while publishing message" } }
    }

    private fun mapToEntity(message: MessageDto) =
        MessageEntity(id = 0L, author = message.author, message = message.message)

    private fun mapToDto(message: MessageEntity) =
        MessageReadDto(author = message.author, message = message.message, createdAt = message.createdAt)
}
