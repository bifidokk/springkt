package springkt

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringktApplication

fun main(args: Array<String>) {
    runApplication<SpringktApplication>(*args)
}

