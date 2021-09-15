package de.dfs.graffitiboard.message.api

import de.dfs.graffitiboard.ResourceTestFixture
import de.dfs.graffitiboard.message.persistence.MessageRepository
import de.dfs.graffitiboard.message.service.Message
import de.dfs.graffitiboard.message.service.MessageService
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.time.ZonedDateTime

@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class MessageControllerITest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val messageService: MessageService,
    @Autowired val messageRepository: MessageRepository
) {
    companion object {
        val message1 = Message(author = null, message = "Message 1")
        val message2 = Message(author = "John Doe", message = "Message 2")

        val messageReadDto1 = MessageReadDto(author = null, message = "Message 1", createdAt = ZonedDateTime.now())
        val messageReadDto2 =
            MessageReadDto(author = "John Doe", message = "Message 2", createdAt = ZonedDateTime.now())
    }

    @BeforeEach
    internal fun setUp() {
        messageService.create(message1)
        messageService.create(message2)
    }

    @Test
    fun `getAll -- it should return unauthoriized if not authentication is given`() {
        mockMvc.get("/messages") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isUnauthorized() }
            content {
                contentType(MediaType.APPLICATION_JSON)
                jsonPath("$", hasSize<Any>(0))
            }
        }
    }

    @Test
    fun `getAll -- it should return all existing messages`() {
        mockMvc.get("/messages") {
            accept = MediaType.APPLICATION_JSON
            headers {
                setBasicAuth("John", "Doe")
            }
        }.andExpect {
            status { isOk() }
            content {
                contentType(MediaType.APPLICATION_JSON)
                jsonPath("$", hasSize<Any>(2))
            }
        }
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "message_valid.json",
            "message_long_valid.json"
        ]
    )
    fun `post -- it should return ok for valid messages`(jsonFile: String) {
        val messageJson = ResourceTestFixture().load(jsonFile)


        mockMvc.post("/messages") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            headers {
                setBasicAuth("John", "Doe")
            }
            content = messageJson
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "message_empty.json",
            "message_invalid.json"
        ]
    )
    fun `post -- it should return bad request for invalid messages`(jsonFile: String) {
        val messageJson = ResourceTestFixture().load(jsonFile)
        requireNotNull(messageJson) { "json file should not be null" }

        mockMvc.post("/messages") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            headers {
                setBasicAuth("John", "Doe")
            }
            content = messageJson
        }.andExpect {
            status { isBadRequest() }
        }
    }
}
