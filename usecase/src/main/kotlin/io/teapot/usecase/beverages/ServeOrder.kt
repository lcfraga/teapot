package io.teapot.usecase.beverages

import io.teapot.domain.entity.Order
import io.teapot.usecase.beverages.port.ClockPort
import io.teapot.usecase.beverages.port.FindOrderPort
import io.teapot.usecase.beverages.port.SaveOrderPort

class ServeOrder(
    private val findOrderPort: FindOrderPort,
    private val clockPort: ClockPort,
    private val saveOrderPort: SaveOrderPort
) {

    fun serve(orderId: String) {
        findOrderPort.findById(orderId).map(this::serve)
    }

    private fun serve(order: Order) {
        if (order.served) {
            return
        }

        saveOrderPort.save(
            order.copy(
                served = true,
                servedAt = clockPort.now()
            )
        )
    }
}
