package io.teapot.application.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.CommonsRequestLoggingFilter

private const val MAX_PAYLOAD_LENGTH = 10000

@Configuration
class RequestLoggingFilterConfiguration {

    @Bean
    fun logFilter(): CommonsRequestLoggingFilter? {
        return CommonsRequestLoggingFilter().apply {
            setIncludeQueryString(true)
            setIncludePayload(true)
            setMaxPayloadLength(MAX_PAYLOAD_LENGTH)
            setIncludeHeaders(true)
            setAfterMessagePrefix("REQUEST DATA: ")
        }
    }
}
