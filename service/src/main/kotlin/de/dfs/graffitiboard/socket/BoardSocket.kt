package de.dfs.graffitiboard.socket

import de.dfs.graffitiboard.message.api.MessageDto
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller

@Controller
class BoardSocket {

    private val messages = mutableListOf<MessageDto>()

    @MessageMapping("/")
    @SendTo("/topic/messages")
    fun greeting(message: MessageDto): List<MessageDto>? {
        Thread.sleep(1000) // simulated delay
        messages.add(message)
        return messages
    }
}
