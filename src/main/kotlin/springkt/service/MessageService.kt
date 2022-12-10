package springkt.service

import org.springframework.stereotype.Service
import springkt.repository.MessageRepository

@Service
class MessageService(
    val messageRepository: MessageRepository
) {

    fun findMessages(): List<Message> {
        return messageRepository.findMessages()
    }

    fun create(message: Message) {
        messageRepository.save(message)
    }
}
