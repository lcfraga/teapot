package io.teapot.usecase.beverages.port

import io.teapot.domain.entity.Order
import io.teapot.domain.entity.OrderSize

interface TeapotPort {
    fun hasEnoughWaterFor(orderSize: OrderSize): Boolean
    fun canBrew(beverage: String): Boolean
    fun brew(order: Order)
}
