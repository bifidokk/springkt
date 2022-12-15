package springkt.service

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.query
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class MessageService(
    val db: JdbcTemplate
) {
    fun findMessages(): List<Message> {
        return db.query("select * from messages") { rs, _ ->
            Message(rs.getString("id"), rs.getString("text"))
        }
    }

    fun findMessageById(id: String): Message? {
        return db.query("select * from messages where id = ?", id) { rs, _ ->
            Message(rs.getString("id"), rs.getString("text"))
        }.firstOrNull()
    }

    fun create(message: Message) {
        db.update("insert into messages values (?, ?)", message.id ?: UUID.randomUUID(), message.text)
    }
}
