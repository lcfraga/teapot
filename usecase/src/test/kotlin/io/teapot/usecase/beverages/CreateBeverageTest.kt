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
import io.teapot.usecase.beverages.port.GenerateIdPort
import io.teapot.usecase.beverages.port.SaveBeveragePort
import io.teapot.usecase.beverages.validator.BeverageWithoutIdValidator
import io.teapot.usecase.validation.ValidationErrors
import java.util.Optional

class CreateBeverageTest : DescribeSpec({

    mockkObject(BeverageWithoutIdValidator)

    val findBeveragePort: FindBeveragePort = mockk()
    val saveBeveragePort: SaveBeveragePort = mockk()
    val generateIdPort: GenerateIdPort = mockk()

    val createBeverage = CreateBeverage(
        findBeveragePort,
        generateIdPort,
        saveBeveragePort
    )

    describe("create") {
        val beverageWithoutId = BeverageWithoutId("Some Beverage")

        describe("when beverage is invalid") {
            val validationErrors: ValidationErrors = mutableMapOf("field" to mutableListOf("error"))

            it("returns CreateBeverageResult.Invalid") {
                every { BeverageWithoutIdValidator.validate(beverageWithoutId) } returns validationErrors

                createBeverage.create(beverageWithoutId) shouldBe CreateBeverageResult.Invalid(validationErrors)

                verify { BeverageWithoutIdValidator.validate(beverageWithoutId) }

                verify {
                    listOf(
                        findBeveragePort,
                        saveBeveragePort,
                        generateIdPort
                    ) wasNot Called
                }

                confirmVerified(
                    BeverageWithoutIdValidator,
                    findBeveragePort,
                    generateIdPort,
                    saveBeveragePort
                )
            }
        }

        describe("when beverage is valid") {
            val emptyValidationErrors: ValidationErrors = mutableMapOf()

            every { BeverageWithoutIdValidator.validate(beverageWithoutId) } returns emptyValidationErrors

            describe("when beverage name is taken") {
                val existingBeverage = Beverage("existing-beverage-id", beverageWithoutId.name)

                it("returns CreateBeverageResult.Conflict") {
                    every {
                        findBeveragePort.findByNameIgnoringCase(beverageWithoutId.name)
                    } returns Optional.of(existingBeverage)

                    createBeverage.create(beverageWithoutId) shouldBe
                        CreateBeverageResult.Conflict("name ${beverageWithoutId.name} is taken")

                    verifyOrder {
                        BeverageWithoutIdValidator.validate(beverageWithoutId)
                        findBeveragePort.findByNameIgnoringCase(beverageWithoutId.name)
                    }

                    verify {
                        listOf(
                            saveBeveragePort,
                            generateIdPort
                        ) wasNot Called
                    }

                    confirmVerified(
                        BeverageWithoutIdValidator,
                        findBeveragePort,
                        generateIdPort,
                        saveBeveragePort
                    )
                }
            }

            describe("when beverage name is not taken") {
                val createdBeverage = Beverage(
                    "created-beverage-id",
                    beverageWithoutId.name
                )

                it("returns CreateBeverageResult.Created") {
                    every { findBeveragePort.findByNameIgnoringCase(beverageWithoutId.name) } returns Optional.empty()
                    every { generateIdPort.generate() } returns createdBeverage.id
                    every { saveBeveragePort.save(createdBeverage) } returns createdBeverage

                    createBeverage.create(beverageWithoutId) shouldBe CreateBeverageResult.Created(createdBeverage)

                    verifyOrder {
                        BeverageWithoutIdValidator.validate(beverageWithoutId)
                        findBeveragePort.findByNameIgnoringCase(beverageWithoutId.name)
                        generateIdPort.generate()
                        saveBeveragePort.save(createdBeverage)
                    }

                    confirmVerified(
                        BeverageWithoutIdValidator,
                        findBeveragePort,
                        generateIdPort,
                        saveBeveragePort
                    )
                }
            }
        }
    }
})
