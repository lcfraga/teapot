package io.teapot.dataprovider.repositories

import io.teapot.dataprovider.entities.OrderEntity
import io.teapot.domain.entity.Order
import io.teapot.domain.entity.OrderSize
import io.teapot.usecase.beverages.port.SaveOrderPort

private fun OrderEntity.toOrder() = Order(
    id,
    beverage,
    settings,
    username,
    OrderSize.valueOf(size),
    createdAt,
    served,
    servedAt
)

private fun Order.toOrderEntity() = OrderEntity(
    id,
    beverage,
    settings,
    username,
    size.toString(),
    createdAt,
    served,
    servedAt
)

class OrdersRepository(
    private val jpaOrdersRepository: JpaOrdersRepository
) : SaveOrderPort {

    override fun save(order: Order): Order {
        return jpaOrdersRepository
            .save(order.toOrderEntity())
            .toOrder()
    }
}
