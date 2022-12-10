package springkt.repository

import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import springkt.service.Message

interface MessageRepository: CrudRepository<Message, String> {

    @Query("select * from messages")
    fun findMessages(): List<Message>
}
