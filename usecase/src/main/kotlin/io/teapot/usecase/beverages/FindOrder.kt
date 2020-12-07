package io.teapot.usecase.beverages

import io.teapot.domain.entity.Order
import io.teapot.usecase.beverages.port.FindOrderPort

sealed class FindOrderResult {
    data class Found(val order: Order) : FindOrderResult()
    object NotFound : FindOrderResult()
}

class FindOrder(private val findOrderPort: FindOrderPort) {

    fun findById(id: String): FindOrderResult {
        return findOrderPort.findById(id)
            .map { found(it) }
            .orElseGet { FindOrderResult.NotFound }
    }

    private fun found(order: Order): FindOrderResult {
        return FindOrderResult.Found(order)
    }
}
