package de.dfs.graffitiboard.message.socket

import com.fasterxml.jackson.databind.ObjectMapper
import de.dfs.graffitiboard.message.api.MessageDto
import de.dfs.graffitiboard.message.api.MessageDtoMapper
import de.dfs.graffitiboard.message.service.Message
import de.dfs.graffitiboard.message.service.MessageService
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException
import javax.validation.Validator

@Component
class TextMessageProcessor(
    private val validator: Validator,
    private val objectMapper: ObjectMapper,
    private val messageService: MessageService,
    private val messageDtoMapper: MessageDtoMapper
) {

    fun processNewConnection(): List<TextMessage> =
        messageService.findAll()
            .map { mapToReadDto(it) }
            .map { TextMessage(objectMapper.writeValueAsString(it)) }

    fun processNewMessage(textMessage: TextMessage): TextMessage {
        val message = objectMapper.readValue(textMessage.payload, MessageDto::class.java)

        val violations = validator.validate(message)
        val createdMessage = when {
            violations.isEmpty() -> messageService.create(mapToMessage(message))
            else -> throw validationError(violations)
        }

        val messageReadDto = mapToReadDto(createdMessage)
        return TextMessage(objectMapper.writeValueAsString(messageReadDto))
    }

    private fun validationError(violations: Set<ConstraintViolation<MessageDto>>): ConstraintViolationException {
        val errorMessage = "Invalid message body: [${violations.joinToString { it.message }}]"
        return ConstraintViolationException("{\"error\": \"$errorMessage\"", violations)
    }

    private fun mapToMessage(message: MessageDto) = messageDtoMapper.mapToMessage(message)
    private fun mapToReadDto(message: Message) = messageDtoMapper.mapToReadDto(message)
}
