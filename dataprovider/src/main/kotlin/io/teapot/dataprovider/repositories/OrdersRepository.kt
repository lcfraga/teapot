package io.teapot.dataprovider.repositories

import io.teapot.dataprovider.entities.OrderEntity
import io.teapot.domain.entity.Order
import io.teapot.domain.entity.OrderSize
import io.teapot.usecase.beverages.port.FindAllOrdersPort
import io.teapot.usecase.beverages.port.FindOrderPort
import io.teapot.usecase.beverages.port.SaveOrderPort
import io.teapot.usecase.pagination.FoundPage
import io.teapot.usecase.pagination.RequestedPage
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
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
) : FindAllOrdersPort, FindOrderPort, SaveOrderPort {

    override fun findAll(requestedPage: RequestedPage): FoundPage<Order> {
        return jpaOrdersRepository
            .findAll(
                PageRequest.of(
                    requestedPage.page - 1, // Spring uses 0-based pagination.
                    requestedPage.size,
                    Sort.by(Sort.Direction.DESC, "createdAt")
                )
            ).toFoundPage(OrderEntity::toOrder)
    }

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
