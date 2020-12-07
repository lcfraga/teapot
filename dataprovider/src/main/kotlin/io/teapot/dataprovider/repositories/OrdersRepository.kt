package io.teapot.dataprovider.repositories

import io.teapot.dataprovider.entities.OrderEntity
import io.teapot.domain.entity.Order
import io.teapot.domain.entity.OrderSize
import io.teapot.usecase.beverages.port.FindOrderPort
import io.teapot.usecase.beverages.port.SaveOrderPort
import java.util.Optional

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
) : FindOrderPort, SaveOrderPort {

    override fun findById(id: String): Optional<Order> {
        return jpaOrdersRepository
            .findById(id)
            .map(OrderEntity::toOrder)
    }

    override fun save(order: Order): Order {
        return jpaOrdersRepository
            .save(order.toOrderEntity())
            .toOrder()
    }
}
