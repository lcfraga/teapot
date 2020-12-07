package io.teapot.domain.entity

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class OrderWithoutId(
    @get:NotBlank
    @get:Size(min = 1, max = 255)
    val beverage: String,

    @get:NotBlank
    @get:Size(min = 1, max = 255)
    val username: String,

    @get:Pattern(regexp = "[SML]", message = "must be S, M or L")
    val size: String
)
