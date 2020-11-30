package io.teapot.application

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class TeapotApplicationTests(
    @LocalServerPort private var port: Int,
    @Autowired val restTemplate: TestRestTemplate
) {
    @Test
    fun healthActuator_reportsStatusUp() {
        data class HealthResponse(val status: String)

        restTemplate.getForEntity<HealthResponse>("http://localhost:$port/actuator/health").apply {
            statusCode.shouldBe(HttpStatus.OK)
            body.shouldBe(HealthResponse("UP"))
        }
    }
}
