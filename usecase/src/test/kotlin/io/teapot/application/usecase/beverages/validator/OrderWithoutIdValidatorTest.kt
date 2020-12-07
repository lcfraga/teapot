package io.teapot.application.usecase.beverages.validator

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.data.row
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldContainKey
import io.kotest.matchers.maps.shouldHaveSize
import io.kotest.matchers.maps.shouldNotBeEmpty
import io.teapot.domain.entity.OrderSize
import io.teapot.domain.entity.OrderWithoutId
import io.teapot.usecase.beverages.validator.OrderWithoutIdValidator
import io.teapot.usecase.validation.ValidationErrors

class OrderWithoutIdValidatorTest : DescribeSpec({

    describe("validate") {
        val validBeverage = "Valid Order"
        val validUsername = "teadrinker"

        describe("when OrderWithoutId is valid") {
            it("returns empty validation errors") {
                val validOrderWithoutId = OrderWithoutId(validBeverage, validUsername, OrderSize.S.toString())
                val validationErrors: ValidationErrors = OrderWithoutIdValidator.validate(validOrderWithoutId)

                validationErrors.shouldBeEmpty()
            }
        }

        describe("when OrderWithoutId is invalid") {
            val emptyString = ""
            val blankString = "     "
            val oversizeString = "a".repeat(256)

            val blankMessage = "must not be blank"
            val oversizeMessage = "size must be between 1 and 255"

            listOf(
                row(
                    "when beverage is empty",
                    "beverage",
                    OrderWithoutId(emptyString, validUsername, OrderSize.S.toString()),
                    listOf(blankMessage, oversizeMessage)
                ),
                row(
                    "when beverage is blank",
                    "beverage",
                    OrderWithoutId(blankString, validUsername, OrderSize.M.toString()),
                    listOf(blankMessage)
                ),
                row(
                    "when beverage has more than 255 characters",
                    "beverage",
                    OrderWithoutId(oversizeString, validUsername, OrderSize.L.toString()),
                    listOf(oversizeMessage)
                ),
                row(
                    "when username is empty",
                    "username",
                    OrderWithoutId(validBeverage, emptyString, OrderSize.L.toString()),
                    listOf(blankMessage, oversizeMessage)
                ),
                row(
                    "when username is blank",
                    "username",
                    OrderWithoutId(validBeverage, blankString, OrderSize.M.toString()),
                    listOf(blankMessage)
                ),
                row(
                    "when username has more than 255 characters",
                    "username",
                    OrderWithoutId(validBeverage, oversizeString, OrderSize.S.toString()),
                    listOf(oversizeMessage)
                ),
                row(
                    "when size is not S, M or L",
                    "size",
                    OrderWithoutId(validBeverage, validUsername, "XL"),
                    listOf("must be S, M or L")
                ),
            ).map { (
                context: String,
                field: String,
                invalidOrderWithoutId: OrderWithoutId,
                expectedErrors: List<String>
            ) ->
                describe(context) {
                    it("returns error messages: $expectedErrors") {
                        val validationErrors: ValidationErrors = OrderWithoutIdValidator
                            .validate(invalidOrderWithoutId)

                        validationErrors.apply {
                            shouldNotBeEmpty()
                            shouldHaveSize(1)
                            shouldContainKey(field)
                        }

                        val actualErrors: List<String> = validationErrors[field]!!

                        actualErrors.shouldContainExactlyInAnyOrder(expectedErrors)
                    }
                }
            }
        }
    }
})
