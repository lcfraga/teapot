package io.teapot.usecase.beverages.port

import java.time.Instant

fun interface ClockPort {
    fun now(): Instant
}
