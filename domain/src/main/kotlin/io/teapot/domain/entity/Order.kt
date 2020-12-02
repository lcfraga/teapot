package io.teapot.domain.entity

import java.time.Instant

data class Order(
    val id: String,
    val beverage: String,
    val settings: Map<String, String>,
    val username: String,
    val size: OrderSize,
    val createdAt: Instant,
    val served: Boolean = false,
    val servedAt: Instant? = null
)
