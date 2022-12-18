package springkt

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.getForObject
import org.springframework.boot.test.web.client.postForObject
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import springkt.service.Message
import java.util.*

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)

@Testcontainers
class SpringktApplicationTests(
    @Autowired val client: TestRestTemplate,
    @Autowired val jdbcTemplate: JdbcTemplate,
) {
    companion object {
        @Container
        val container = postgres("postgres:13-alpine") {
            withDatabaseName("db")
            withUsername("user")
            withPassword("password")
            withInitScript("sql/schema.sql")
        }

        @JvmStatic
        @DynamicPropertySource
        fun dataSourceConfig(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", container::getJdbcUrl)
            registry.add("spring.datasource.password", container::getPassword)
            registry.add("spring.datasource.username", container::getUsername)
        }
    }

    @AfterEach
    fun cleanup() {
        jdbcTemplate.execute("truncate table messages")
    }


    @Test
    fun `test hello endpoint`() {
        val entity = client.getForEntity<String>("/hello")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).contains("Hello")
    }

    @Test
    fun `test it creates a new message`() {
        val id = UUID.randomUUID().toString()
        val message = Message(id, "Hello")

        client.postForObject<Message>("/", message)

        val entity = client.getForEntity<String>("/$id")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).contains(message.id)
        assertThat(entity.body).contains(message.text)

        val messageObject = client.getForObject<Message>("/$id")!!
        assertThat(messageObject.id).contains(message.id)
        assertThat(messageObject.text).contains(message.text)
    }

    @Test
    fun `test it return not found error if message doe not exist`() {
        val id = UUID.randomUUID().toString()

        val entity = client.getForEntity<String>("/$id")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }
}
