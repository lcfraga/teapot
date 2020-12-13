package io.teapot.application.event

import io.teapot.usecase.beverages.ServeOrder
import org.springframework.scheduling.annotation.Async
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionalEventListener

open class AsyncOrderCreatedEventListener(private val serveOrder: ServeOrder) : OrderCreatedEventListener {

    @TransactionalEventListener
    @Async
    @Transactional
    override fun process(event: OrderCreatedEvent) = serveOrder.serve(event.orderId)
}
