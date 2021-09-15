package de.dfs.graffitiboard.message.socket

import io.mockk.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

internal class MessageSocketHandlerTest {

    companion object {
        val session1 = mockk<WebSocketSession>()
        val session2 = mockk<WebSocketSession>()
    }

    private val textMessageProcessor = mockk<TextMessageProcessor>()
    private val sut = MessageSocketHandler(textMessageProcessor)

    @BeforeEach
    internal fun setUp() {
        every { session1.sendMessage(any()) } just Runs
        every { session2.sendMessage(any()) } just Runs
        every { textMessageProcessor.processNewConnection() } returns emptyList()
    }

    @Test
    internal fun `afterConnectionEstablished - it should emit already received messages to newly connected client`() {
        val textMessage1 = TextMessage("message_json")
        val textMessage2 = TextMessage("message_json")

        every { textMessageProcessor.processNewConnection() } returns listOf(textMessage1, textMessage2)

        sut.afterConnectionEstablished(session1)

        verify {
            session1.sendMessage(textMessage1)
            session1.sendMessage(textMessage2)
        }
    }

    @Test
    internal fun `handleMessage - an incoming message should be processed and broadcasted to all connected clients`() {
        sut.afterConnectionEstablished(session1)
        sut.afterConnectionEstablished(session2)

        val incomingTextMessage = TextMessage("message_json")
        val processedTextMessage = TextMessage("processed_message")
        every { textMessageProcessor.processNewMessage(incomingTextMessage) } returns processedTextMessage

        sut.handleMessage(session1, incomingTextMessage)

        verify {
            textMessageProcessor.processNewMessage(incomingTextMessage)
            session1.sendMessage(processedTextMessage)
            session2.sendMessage(processedTextMessage)
        }
    }

    @Test
    internal fun `handleMessage - when message processing fails then an error should be returned to emitter only`() {
        sut.afterConnectionEstablished(session1)
        sut.afterConnectionEstablished(session2)

        val incomingTextMessage = TextMessage("message_json")
        every { textMessageProcessor.processNewMessage(incomingTextMessage) } throws RuntimeException("something went wrong")

        sut.handleMessage(session1, incomingTextMessage)

        val slot = slot<TextMessage>()
        verify { session1.sendMessage(capture(slot)) }

        assertThat(slot.captured.payload).isEqualTo("something went wrong")

        verify(exactly = 0) { session2.sendMessage(any()) }
    }
}
