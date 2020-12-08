package io.teapot.usecase.beverages

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.Called
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import io.mockk.verifyOrder
import io.teapot.domain.entity.Beverage
import io.teapot.domain.entity.BeverageWithoutId
import io.teapot.usecase.beverages.port.FindBeveragePort
import io.teapot.usecase.beverages.port.SaveBeveragePort
import io.teapot.usecase.beverages.validator.BeverageWithoutIdValidator
import io.teapot.usecase.validation.ValidationErrors
import java.util.Optional

class UpdateBeverageTest : DescribeSpec({

    mockkObject(BeverageWithoutIdValidator)

    val findBeveragePort: FindBeveragePort = mockk()
    val saveBeveragePort: SaveBeveragePort = mockk()

    val updateBeverage = UpdateBeverage(findBeveragePort, saveBeveragePort)

    describe("update") {
        val beverageId = "beverage-id"
        val beverageWithoutId = BeverageWithoutId("Some Beverage", mapOf("setting" to "value"))

        describe("when beverage is invalid") {
            val validationErrors: ValidationErrors = mutableMapOf("field" to mutableListOf("error"))
            val expectedResult = UpdateBeverageResult.Invalid(validationErrors)

            every { BeverageWithoutIdValidator.validate(beverageWithoutId) } returns validationErrors

            it("returns UpdateBeverageResult.Invalid") {
                updateBeverage.update(beverageId, beverageWithoutId) shouldBe expectedResult

                verify { BeverageWithoutIdValidator.validate(beverageWithoutId) }

                verify {
                    listOf(
                        findBeveragePort,
                        saveBeveragePort
                    ) wasNot Called
                }

                confirmVerified(
                    BeverageWithoutIdValidator,
                    findBeveragePort,
                    saveBeveragePort
                )
            }
        }

        describe("when beverage is valid") {
            val emptyValidationErrors: ValidationErrors = mutableMapOf()

            every { BeverageWithoutIdValidator.validate(beverageWithoutId) } returns emptyValidationErrors

            describe("when beverage does not exist") {
                every { findBeveragePort.findById(beverageId) } returns Optional.empty()

                it("returns UpdateBeverageResult.NotFound") {
                    updateBeverage.update(beverageId, beverageWithoutId) shouldBe UpdateBeverageResult.NotFound

                    verifyOrder {
                        BeverageWithoutIdValidator.validate(beverageWithoutId)
                        findBeveragePort.findById(beverageId)
                    }

                    verify { saveBeveragePort wasNot Called }

                    confirmVerified(
                        BeverageWithoutIdValidator,
                        findBeveragePort,
                        saveBeveragePort
                    )
                }
            }

            describe("when beverage exists") {
                val existingBeverage = Beverage(
                    beverageId,
                    beverageWithoutId.name,
                )

                every { findBeveragePort.findById(beverageId) } returns Optional.of(existingBeverage)

                describe("when beverage name is taken by another beverage") {
                    val otherBeverageWithSameName = Beverage(
                        "$beverageId $beverageId",
                        beverageWithoutId.name
                    )

                    every {
                        findBeveragePort.findByNameIgnoringCase(beverageWithoutId.name)
                    } returns Optional.of(otherBeverageWithSameName)

                    it("returns UpdateBeverageResult.Conflict") {
                        updateBeverage.update(beverageId, beverageWithoutId) shouldBe
                            UpdateBeverageResult.Conflict("name ${beverageWithoutId.name} is taken")

                        verifyOrder {
                            BeverageWithoutIdValidator.validate(beverageWithoutId)
                            findBeveragePort.findById(beverageId)
                            findBeveragePort.findByNameIgnoringCase(beverageWithoutId.name)
                        }

                        verify { saveBeveragePort wasNot Called }

                        confirmVerified(
                            BeverageWithoutIdValidator,
                            findBeveragePort,
                            saveBeveragePort
                        )
                    }
                }

                describe("successful update") {
                    val updatedBeverage = Beverage(
                        beverageId,
                        beverageWithoutId.name,
                        beverageWithoutId.settings
                    )

                    every { saveBeveragePort.save(updatedBeverage) } returns updatedBeverage

                    describe("when beverage name is taken by same beverage") {
                        every {
                            findBeveragePort.findByNameIgnoringCase(beverageWithoutId.name)
                        } returns Optional.of(existingBeverage)

                        it("returns UpdateBeverageResult.Updated") {
                            updateBeverage.update(beverageId, beverageWithoutId) shouldBe
                                UpdateBeverageResult.Updated(updatedBeverage)

                            verifyOrder {
                                BeverageWithoutIdValidator.validate(beverageWithoutId)
                                findBeveragePort.findById(beverageId)
                                findBeveragePort.findByNameIgnoringCase(beverageWithoutId.name)
                                saveBeveragePort.save(updatedBeverage)
                            }

                            confirmVerified(
                                BeverageWithoutIdValidator,
                                findBeveragePort,
                                saveBeveragePort
                            )
                        }
                    }

                    describe("when beverage name is not taken") {
                        every {
                            findBeveragePort.findByNameIgnoringCase(beverageWithoutId.name)
                        } returns Optional.empty()

                        it("returns UpdateBeverageResult.Updated") {
                            updateBeverage.update(beverageId, beverageWithoutId) shouldBe
                                UpdateBeverageResult.Updated(updatedBeverage)

                            verifyOrder {
                                BeverageWithoutIdValidator.validate(beverageWithoutId)
                                findBeveragePort.findById(beverageId)
                                findBeveragePort.findByNameIgnoringCase(beverageWithoutId.name)
                                saveBeveragePort.save(updatedBeverage)
                            }

                            confirmVerified(
                                BeverageWithoutIdValidator,
                                findBeveragePort,
                                saveBeveragePort
                            )
                        }
                    }
                }
            }
        }
    }
})
