package springkt

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import springkt.service.Message
import springkt.service.MessageService

@WebMvcTest
class WebTierTests(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    private lateinit var messageService: MessageService

    @Test
    fun `test it finds messages`() {
        every { messageService.findMessages() } returns listOf(
            Message("1", "Hello"),
            Message("2", "World")
        )

        mockMvc.get("/")
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andExpect {
                jsonPath("\$.[0].id") { value("1") }
                jsonPath("\$.[0].text") { value("Hello") }
                jsonPath("\$.[1].id") { value("2") }
                jsonPath("\$.[1].text") { value("World") }
            }

        verify(exactly = 1) { messageService.findMessages() }
    }
}