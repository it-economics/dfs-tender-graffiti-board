package de.dfs.graffitiboard.message.socket

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import de.dfs.graffitiboard.message.api.MessageDto
import de.dfs.graffitiboard.message.service.MessageService
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException
import javax.validation.Validator

@Component
class TextMessageProcessor(
    private val objectMapper: ObjectMapper,
    private val messageService: MessageService,
    private val validator: Validator
) {

    fun process(textMessage: TextMessage): TextMessage {
        val message = objectMapper.readValue<MessageDto>(textMessage.payload)

        val violations = validator.validate(message)
        val createdMessage = when {
            violations.isEmpty() -> messageService.create(message)
            else -> throw validationError(violations)
        }

        return TextMessage(objectMapper.writeValueAsString(createdMessage))
    }

    private fun validationError(violations: Set<ConstraintViolation<MessageDto>>): ConstraintViolationException {
        val errorMessage = "Invalid message body: [${violations.joinToString { it.message }}]"
        // TODO Define Error response 
        return ConstraintViolationException("{\"error\": \"$errorMessage\"", violations)
    }
}
