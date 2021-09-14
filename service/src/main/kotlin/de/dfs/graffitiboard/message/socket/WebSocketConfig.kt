package de.dfs.graffitiboard.message.socket

import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
class WebSocketConfig(
    private val textMessageProcessor: TextMessageProcessor
) : WebSocketConfigurer {

    override fun registerWebSocketHandlers(webSocketHandlerRegistry: WebSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(MessageSocketHandler(textMessageProcessor), "/message-socket")
    }
}
