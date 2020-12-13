package io.teapot.application.event

data class OrderCreatedEvent(val orderId: String)

interface OrderCreatedEventListener {
    fun process(event: OrderCreatedEvent)
}
