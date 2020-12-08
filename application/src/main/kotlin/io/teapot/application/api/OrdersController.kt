package io.teapot.application.api

import io.teapot.domain.entity.Order
import io.teapot.domain.entity.OrderWithoutId
import io.teapot.usecase.beverages.FindAllOrders
import io.teapot.usecase.beverages.FindAllOrdersResult
import io.teapot.usecase.beverages.FindOrder
import io.teapot.usecase.beverages.FindOrderResult
import io.teapot.usecase.beverages.OrderBeverage
import io.teapot.usecase.beverages.OrderBeverageResult
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

private fun OrderBeverageRequest.toOrderWithoutId() = OrderWithoutId(
    beverage,
    username,
    size.toUpperCase()
)

private fun Order.toOrderResponseBody() = OrderResponseBody(
    id,
    beverage,
    settings,
    username,
    size.toString(),
    createdAt,
    served,
    servedAt
)

@RestController
class OrdersController(
    private val findAllOrders: FindAllOrders,
    private val findOrder: FindOrder,
    private val orderBeverage: OrderBeverage
) : OrdersApi {

    override fun findAll(paginationParameters: PaginationParameters): PaginatedResponse<OrderResponseBody> {
        return when (val findAllOrdersResult = findAllOrders.findAll(paginationParameters.toRequestedPage())) {
            is FindAllOrdersResult.Found ->
                findAllOrdersResult.orders
                    .toPaginatedResponse(Order::toOrderResponseBody)
        }
    }

    override fun findById(id: String): ResponseEntity<Any> {
        return when (val findOrderResult = findOrder.findById(id)) {
            is FindOrderResult.Found ->
                ResponseEntity
                    .status(HttpStatus.OK)
                    .body(findOrderResult.order.toOrderResponseBody())
            is FindOrderResult.NotFound ->
                ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build()
        }
    }

    override fun create(orderBeverageRequest: OrderBeverageRequest): ResponseEntity<Any> {
        return when (val orderBeverageResult = orderBeverage.order(orderBeverageRequest.toOrderWithoutId())) {
            is OrderBeverageResult.Ordered ->
                ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(orderBeverageResult.order.toOrderResponseBody())
            is OrderBeverageResult.Invalid ->
                ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(orderBeverageResult.errors)
            is OrderBeverageResult.BeverageNotFound ->
                ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build()
            is OrderBeverageResult.Rejected ->
                ResponseEntity
                    .status(HttpStatus.I_AM_A_TEAPOT)
                    .build()
            is OrderBeverageResult.InsufficientWater ->
                ResponseEntity
                    .status(HttpStatus.TOO_MANY_REQUESTS)
                    .build()
        }
    }
}
