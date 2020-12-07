package io.teapot.domain.entity

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class BeverageWithoutId(
    @get:NotBlank
    @get:Size(min = 1, max = 255)
    val name: String,

    @get:Size(max = 5)
    val settings: Map<
        @NotBlank @Size(min = 1, max = 255) String,
        @NotBlank @Size(min = 1, max = 255) String
        > = emptyMap()
)
