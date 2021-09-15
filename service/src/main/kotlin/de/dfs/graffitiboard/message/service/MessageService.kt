package de.dfs.graffitiboard.message.service

import de.dfs.graffitiboard.message.persistence.MessageEntity
import de.dfs.graffitiboard.message.persistence.MessageRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import reactor.core.publisher.Sinks

@Service
class MessageService(
    private val messageRepository: MessageRepository,
    private val messageSink: Sinks.Many<Message>
) {
    companion object {
        private val log = KotlinLogging.logger { }
    }

    fun create(message: Message): Message =
        run { mapToEntity(message) }
            .let { messageRepository.save(it) }
            .let { mapToMessage(it) }
            .also { publish(it) }

    fun findAll() = messageRepository.findByOrderByCreatedAtAsc().map { mapToMessage(it) }

    fun publish(message: Message) {
        runCatching { messageSink.tryEmitNext(message) }
            .onSuccess { log.info("Published new message") }
            .onFailure { e -> log.warn(e) { "Error while publishing message" } }
    }

    private fun mapToEntity(message: Message) =
        MessageEntity(id = 0L, author = message.author, message = message.message)

    private fun mapToMessage(message: MessageEntity) =
        Message(author = message.author, message = message.message, createdAt = message.createdAt)
}
