package de.dfs.graffitiboard.message.socket

import mu.KotlinLogging
import org.springframework.validation.annotation.Validated
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.concurrent.CopyOnWriteArrayList
import java.util.function.Consumer

@Validated
class MessageSocketHandler(
    private val textMessageProcessor: TextMessageProcessor
) : TextWebSocketHandler() {

    companion object {
        private val log = KotlinLogging.logger { }
    }

    private val sessions: MutableList<WebSocketSession> = CopyOnWriteArrayList()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        log.info { "User '${session.principal?.name}' establishes socket connection" }
        sessions.add(session)
        super.afterConnectionEstablished(session)
        textMessageProcessor.processNewConnection().forEach { session.sendMessage(it) }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        log.info { "User '${session.principal?.name}' closes socket connection" }
        sessions.remove(session)
        super.afterConnectionClosed(session, status)
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        super.handleTextMessage(session, message)

        runCatching { textMessageProcessor.processNewMessage(message) }
            .onSuccess {
                log.info { "Broadcasting message to connected subscribers" }
                sessions.forEach(sendMessage(message))
            }
            .onFailure {
                log.info { "Error while process incoming text message: ${it.message}" }
                session.sendMessage(TextMessage(it.message!!))
            }
    }

    private fun sendMessage(message: TextMessage) =
        Consumer { webSocketSession: WebSocketSession ->
            runCatching { webSocketSession.sendMessage(message) }
                .onFailure { log.error(it.cause) { "Error occurred: ${it.message}" } }
        }
}
