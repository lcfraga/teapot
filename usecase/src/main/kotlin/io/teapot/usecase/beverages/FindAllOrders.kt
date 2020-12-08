package io.teapot.usecase.beverages

import io.teapot.domain.entity.Order
import io.teapot.usecase.beverages.port.FindAllOrdersPort
import io.teapot.usecase.pagination.FoundPage
import io.teapot.usecase.pagination.RequestedPage

sealed class FindAllOrdersResult {
    data class Found(val orders: FoundPage<Order>) : FindAllOrdersResult()
}

class FindAllOrders(private val findAllOrdersPort: FindAllOrdersPort) {

    fun findAll(requestedPage: RequestedPage): FindAllOrdersResult {
        return FindAllOrdersResult.Found(
            findAllOrdersPort.findAll(requestedPage.safeCopy())
        )
    }
}
