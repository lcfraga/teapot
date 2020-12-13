package io.teapot.usecase.beverages

import io.kotest.core.spec.style.DescribeSpec
import io.mockk.Called
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import io.teapot.domain.entity.Order
import io.teapot.domain.entity.OrderSize
import io.teapot.usecase.beverages.port.ClockPort
import io.teapot.usecase.beverages.port.FindOrderPort
import io.teapot.usecase.beverages.port.SaveOrderPort
import java.time.Instant
import java.util.Optional

class ServeOrderTest : DescribeSpec({

    val findOrderPort: FindOrderPort = mockk()
    val saveOrderPort: SaveOrderPort = mockk()
    val clockPort: ClockPort = mockk()

    val serveOrder = ServeOrder(findOrderPort, clockPort, saveOrderPort)

    describe("serve") {
        val orderId = "order-id"

        describe("when order does not exist") {

            it("is not served") {
                every { findOrderPort.findById(orderId) } returns Optional.empty()

                serveOrder.serve(orderId)

                verify { findOrderPort.findById(orderId) }

                verify {
                    listOf(clockPort, saveOrderPort) wasNot Called
                }

                confirmVerified(findOrderPort, clockPort, saveOrderPort)
            }
        }

        describe("when order exists") {
            val order = Order(orderId, "chai tea", emptyMap(), "J. Doe", OrderSize.M, Instant.now())
            val servedAt = Instant.now()
            val servedOrder = order.copy(served = true, servedAt = servedAt)

            describe("when order is served") {

                it("order is not served again") {
                    every { findOrderPort.findById(orderId) } returns Optional.of(servedOrder)

                    serveOrder.serve(orderId)

                    verify { findOrderPort.findById(orderId) }

                    verify {
                        listOf(clockPort, saveOrderPort) wasNot Called
                    }

                    confirmVerified(findOrderPort, clockPort, saveOrderPort)
                }
            }

            describe("when order is not served") {
                it("order is served") {
                    every { findOrderPort.findById(orderId) } returns Optional.of(order)
                    every { clockPort.now() } returns servedAt
                    every { saveOrderPort.save(servedOrder) } returns servedOrder

                    serveOrder.serve(orderId)

                    verifyOrder {
                        findOrderPort.findById(orderId)
                        clockPort.now()
                        saveOrderPort.save(servedOrder)
                    }

                    confirmVerified(findOrderPort, clockPort, saveOrderPort)
                }
            }
        }
    }
})
