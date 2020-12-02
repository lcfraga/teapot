package io.teapot.domain.entity

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class OrderWithoutId(
    @get:NotBlank
    val beverage: String,
    @get:NotBlank
    val username: String,
    @get:Pattern(regexp = "[SML]", message = "must be S, M or L")
    val size: String
)
