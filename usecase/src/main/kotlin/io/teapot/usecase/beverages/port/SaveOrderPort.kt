package io.teapot.usecase.beverages.port

import io.teapot.domain.entity.Order

interface SaveOrderPort {
    fun save(order: Order): Order
}
