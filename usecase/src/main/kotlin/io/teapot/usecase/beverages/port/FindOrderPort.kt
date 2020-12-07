package io.teapot.usecase.beverages.port

import io.teapot.domain.entity.Order
import java.util.Optional

interface FindOrderPort {
    fun findById(id: String): Optional<Order>
}
