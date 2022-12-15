package springkt.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import springkt.service.Message
import springkt.service.MessageService

@RestController
class MessageResource (val messageService: MessageService) {

    @GetMapping("/")
    fun index(): List<Message> = messageService.findMessages()

    @GetMapping("/{id}")
    fun view(@PathVariable id: String): Message = messageService.findMessageById(id)
        ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    @PostMapping("/")
    fun create(@RequestBody message: Message) {
        messageService.create(message)
    }
}