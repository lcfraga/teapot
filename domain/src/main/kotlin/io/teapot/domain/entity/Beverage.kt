package io.teapot.domain.entity

data class Beverage(
    val id: String,
    val name: String,
    val settings: Map<String, String> = emptyMap()
)
