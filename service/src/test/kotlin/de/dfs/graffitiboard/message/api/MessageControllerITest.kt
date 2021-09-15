package de.dfs.graffitiboard.message.api

import de.dfs.graffitiboard.ResourceTestFixture
import de.dfs.graffitiboard.message.persistence.MessageEntity
import de.dfs.graffitiboard.message.persistence.MessageRepository
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.AfterEach
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

@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class MessageControllerITest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val messageRepository: MessageRepository
) {
    companion object {
        val message1 = MessageEntity(id = 0, author = null, message = "Message 1")
        val message2 = MessageEntity(id = 0, author = "John Doe", message = "Message 2")
    }

    @BeforeEach
    internal fun setUp() {
        messageRepository.saveAll(listOf(message1, message2))
    }

    @AfterEach
    internal fun tearDown() {
        messageRepository.deleteAll()
    }

    @Test
    fun `getAll -- it should return unauthorized if not authentication is given`() {
        mockMvc.get("/messages") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isUnauthorized() }
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

        assertThat(messageRepository.findAll()).hasSize(3)
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

        assertThat(messageRepository.findAll()).hasSize(2)
    }
}
