package io.teapot.usecase.beverages

import io.teapot.domain.entity.Beverage
import io.teapot.domain.entity.Order
import io.teapot.domain.entity.OrderSize
import io.teapot.domain.entity.OrderWithoutId
import io.teapot.usecase.beverages.port.ClockPort
import io.teapot.usecase.beverages.port.FindBeveragePort
import io.teapot.usecase.beverages.port.GenerateIdPort
import io.teapot.usecase.beverages.port.SaveOrderPort
import io.teapot.usecase.beverages.port.TeapotPort
import io.teapot.usecase.beverages.validator.OrderWithoutIdValidator
import io.teapot.usecase.validation.ValidationErrors

sealed class OrderBeverageResult {
    data class Ordered(val order: Order) : OrderBeverageResult()
    data class Invalid(val errors: ValidationErrors) : OrderBeverageResult()
    object BeverageNotFound : OrderBeverageResult()
    object Rejected : OrderBeverageResult()
    object InsufficientWater : OrderBeverageResult()
}

class OrderBeverage(
    private val findBeveragePort: FindBeveragePort,
    private val teapotPort: TeapotPort,
    private val generateIdPort: GenerateIdPort,
    private val clockPort: ClockPort,
    private val saveOrderPort: SaveOrderPort
) {

    fun order(orderWithoutId: OrderWithoutId): OrderBeverageResult {
        val validationErrors: ValidationErrors = OrderWithoutIdValidator.validate(orderWithoutId)

        if (validationErrors.isNotEmpty()) {
            return OrderBeverageResult.Invalid(validationErrors)
        }

        return findBeveragePort.findByNameIgnoringCase(orderWithoutId.beverage)
            .map { order(orderWithoutId, it) }
            .orElseGet { OrderBeverageResult.BeverageNotFound }
    }

    private fun order(orderWithoutId: OrderWithoutId, beverage: Beverage): OrderBeverageResult {

        val orderSize = OrderSize.valueOf(orderWithoutId.size)

        if (!teapotPort.canBrew(orderWithoutId.beverage)) {
            return OrderBeverageResult.Rejected
        }

        if (!teapotPort.hasEnoughWaterFor(orderSize)) {
            return OrderBeverageResult.InsufficientWater
        }

        val createdOrder: Order = saveOrderPort.save(
            Order(
                generateIdPort.generate(),
                beverage.name,
                beverage.settings,
                orderWithoutId.username,
                orderSize,
                clockPort.now()
            )
        )

        teapotPort.brew(createdOrder)

        return OrderBeverageResult.Ordered(createdOrder)
    }
}
