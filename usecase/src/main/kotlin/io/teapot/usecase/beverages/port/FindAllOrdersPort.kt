package io.teapot.usecase.beverages.port

import io.teapot.domain.entity.Order
import io.teapot.usecase.pagination.FoundPage
import io.teapot.usecase.pagination.RequestedPage

interface FindAllOrdersPort {
    fun findAll(requestedPage: RequestedPage): FoundPage<Order>
}
