package io.teapot.application.usecase.beverages

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.Called
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import io.mockk.verifyOrder
import io.teapot.domain.entity.Beverage
import io.teapot.domain.entity.Order
import io.teapot.domain.entity.OrderSize
import io.teapot.domain.entity.OrderWithoutId
import io.teapot.usecase.beverages.OrderBeverage
import io.teapot.usecase.beverages.OrderBeverageResult
import io.teapot.usecase.beverages.port.ClockPort
import io.teapot.usecase.beverages.port.FindBeveragePort
import io.teapot.usecase.beverages.port.GenerateIdPort
import io.teapot.usecase.beverages.port.SaveOrderPort
import io.teapot.usecase.beverages.port.TeapotPort
import io.teapot.usecase.beverages.validator.OrderWithoutIdValidator
import io.teapot.usecase.validation.ValidationErrors
import java.time.Instant
import java.util.Optional

class OrderBeverageTest : DescribeSpec({

    mockkObject(OrderWithoutIdValidator)

    val findBeveragePort: FindBeveragePort = mockk()
    val teapotPort: TeapotPort = mockk()
    val generateIdPort: GenerateIdPort = mockk()
    val clockPort: ClockPort = mockk()
    val saveOrderPort: SaveOrderPort = mockk()

    val orderBeverage = OrderBeverage(
        findBeveragePort,
        teapotPort,
        generateIdPort,
        clockPort,
        saveOrderPort
    )

    describe("order") {
        val orderWithoutId = OrderWithoutId("chai tea", "Susan S.", "S")

        describe("when order is invalid") {
            val validationErrors: ValidationErrors = mutableMapOf("field" to mutableListOf("error"))

            every { OrderWithoutIdValidator.validate(orderWithoutId) } returns validationErrors

            it("returns OrderBeverageResult.Invalid") {
                orderBeverage.order(orderWithoutId) shouldBe OrderBeverageResult.Invalid(validationErrors)

                verify { OrderWithoutIdValidator.validate(orderWithoutId) }

                verify {
                    listOf(
                        findBeveragePort,
                        teapotPort,
                        generateIdPort,
                        clockPort,
                        saveOrderPort
                    ) wasNot Called
                }

                confirmVerified(
                    OrderWithoutIdValidator,
                    findBeveragePort,
                    teapotPort,
                    generateIdPort,
                    clockPort,
                    saveOrderPort
                )
            }
        }

        describe("when order is valid") {
            val emptyValidationErrors: ValidationErrors = mutableMapOf()

            every { OrderWithoutIdValidator.validate(orderWithoutId) } returns emptyValidationErrors

            describe("when beverage does not exist") {
                every { findBeveragePort.findByNameIgnoringCase(orderWithoutId.beverage) } returns Optional.empty()

                it("returns OrderBeverageResult.BeverageNotFound") {
                    orderBeverage.order(orderWithoutId) shouldBe OrderBeverageResult.BeverageNotFound

                    verifyOrder {
                        OrderWithoutIdValidator.validate(orderWithoutId)
                        findBeveragePort.findByNameIgnoringCase(orderWithoutId.beverage)
                    }

                    verify {
                        listOf(
                            teapotPort,
                            generateIdPort,
                            clockPort,
                            saveOrderPort
                        ) wasNot Called
                    }

                    confirmVerified(
                        OrderWithoutIdValidator,
                        findBeveragePort,
                        teapotPort,
                        generateIdPort,
                        clockPort,
                        saveOrderPort
                    )
                }
            }

            describe("when beverage exists") {
                val existingBeverage = Beverage("id-1", orderWithoutId.beverage, mapOf("setting" to "value"))

                every {
                    findBeveragePort.findByNameIgnoringCase(orderWithoutId.beverage)
                } returns Optional.of(existingBeverage)

                describe("when teapot cannot brew beverage") {
                    every { teapotPort.canBrew(existingBeverage.name) } returns false

                    it("returns OrderBeverageResult.Rejected") {
                        orderBeverage.order(orderWithoutId) shouldBe OrderBeverageResult.Rejected

                        verifyOrder {
                            OrderWithoutIdValidator.validate(orderWithoutId)
                            findBeveragePort.findByNameIgnoringCase(orderWithoutId.beverage)
                            teapotPort.canBrew(existingBeverage.name)
                        }

                        verify {
                            listOf(
                                generateIdPort,
                                clockPort,
                                saveOrderPort
                            ) wasNot Called
                        }

                        confirmVerified(
                            OrderWithoutIdValidator,
                            findBeveragePort,
                            teapotPort,
                            generateIdPort,
                            clockPort,
                            saveOrderPort
                        )
                    }
                }

                describe("when teapot can brew beverage") {
                    val orderSize = OrderSize.valueOf(orderWithoutId.size)

                    every { teapotPort.canBrew(existingBeverage.name) } returns true

                    describe("when teapot does not have enough water") {
                        every { teapotPort.hasEnoughWaterFor(orderSize) } returns false

                        it("returns OrderBeverageResult.InsufficientWater") {
                            orderBeverage.order(orderWithoutId) shouldBe OrderBeverageResult.InsufficientWater

                            verifyOrder {
                                OrderWithoutIdValidator.validate(orderWithoutId)
                                findBeveragePort.findByNameIgnoringCase(orderWithoutId.beverage)
                                teapotPort.canBrew(existingBeverage.name)
                                teapotPort.hasEnoughWaterFor(orderSize)
                            }

                            verify {
                                listOf(
                                    generateIdPort,
                                    clockPort,
                                    saveOrderPort
                                ) wasNot Called
                            }

                            confirmVerified(
                                OrderWithoutIdValidator,
                                findBeveragePort,
                                teapotPort,
                                generateIdPort,
                                clockPort,
                                saveOrderPort
                            )
                        }
                    }

                    describe("when teapot has enough water") {
                        val createdAt = Instant.now()
                        val orderId = "order-id"

                        val createdOrder = Order(
                            orderId,
                            existingBeverage.name,
                            existingBeverage.settings,
                            orderWithoutId.username,
                            orderSize,
                            createdAt
                        )

                        every { teapotPort.hasEnoughWaterFor(orderSize) } returns true

                        it("returns OrderBeverageResult.Created") {
                            every { generateIdPort.generate() } returns orderId
                            every { clockPort.now() } returns createdAt
                            every { saveOrderPort.save(createdOrder) } returns createdOrder
                            justRun { teapotPort.brew(createdOrder) }

                            orderBeverage.order(orderWithoutId) shouldBe OrderBeverageResult.Ordered(createdOrder)

                            verifyOrder {
                                OrderWithoutIdValidator.validate(orderWithoutId)
                                findBeveragePort.findByNameIgnoringCase(orderWithoutId.beverage)
                                teapotPort.canBrew(existingBeverage.name)
                                teapotPort.hasEnoughWaterFor(orderSize)
                                generateIdPort.generate()
                                clockPort.now()
                                saveOrderPort.save(createdOrder)
                                teapotPort.brew(createdOrder)
                            }

                            confirmVerified(
                                OrderWithoutIdValidator,
                                findBeveragePort,
                                teapotPort,
                                generateIdPort,
                                clockPort,
                                saveOrderPort
                            )
                        }
                    }
                }
            }
        }
    }
})
