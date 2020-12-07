package io.teapot.application.usecase.beverages

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.teapot.domain.entity.Order
import io.teapot.domain.entity.OrderSize
import io.teapot.usecase.beverages.FindOrder
import io.teapot.usecase.beverages.FindOrderResult
import io.teapot.usecase.beverages.port.FindOrderPort
import java.time.Instant
import java.util.Optional

class FindOrderTest : DescribeSpec({

    val findOrderPort: FindOrderPort = mockk()
    val findOrder = FindOrder(findOrderPort)

    describe("findById") {
        val orderId = "order-id"

        describe("when order exists") {
            val order = Order(orderId, "green tea", emptyMap(), "teaperson", OrderSize.L, Instant.now())

            every { findOrderPort.findById(orderId) } returns Optional.of(order)

            it("returns FindOrderResult.Found") {
                findOrder.findById(orderId) shouldBe FindOrderResult.Found(order)

                verify { findOrderPort.findById(orderId) }
                confirmVerified(findOrderPort)
            }
        }

        describe("when order does not exist") {
            every { findOrderPort.findById(orderId) } returns Optional.empty()

            it("returns FindOrderResult.NotFound") {
                findOrder.findById(orderId) shouldBe FindOrderResult.NotFound

                verify { findOrderPort.findById(orderId) }
                confirmVerified(findOrderPort)
            }
        }
    }
})
