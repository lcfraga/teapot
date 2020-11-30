package io.teapot.application

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackages = [
        "io.teapot.application.config"
    ]
)
class TeapotApplication

fun main(args: Array<String>) {
    runApplication<TeapotApplication>(*args)
}
