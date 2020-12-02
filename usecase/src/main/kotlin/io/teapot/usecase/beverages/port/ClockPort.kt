package io.teapot.usecase.beverages.port

import java.time.Instant

interface ClockPort {
    fun now(): Instant = Instant.now()
}
