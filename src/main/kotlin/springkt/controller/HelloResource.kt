package springkt.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class HelloResource {
    @GetMapping("/hello")
    @ResponseBody
    fun index(): String = "Hello"
}