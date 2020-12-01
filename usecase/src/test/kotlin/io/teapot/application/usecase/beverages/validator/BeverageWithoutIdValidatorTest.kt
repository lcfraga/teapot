package io.teapot.application.usecase.beverages.validator

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.data.row
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldContainKey
import io.kotest.matchers.maps.shouldHaveSize
import io.kotest.matchers.maps.shouldNotBeEmpty
import io.teapot.domain.entity.BeverageWithoutId
import io.teapot.usecase.beverages.validator.BeverageWithoutIdValidator
import io.teapot.usecase.validation.ValidationErrors

class BeverageWithoutIdValidatorTest : DescribeSpec({

    describe("validate") {
        val validName = "Valid Beverage"

        describe("when BeverageWithoutId is valid") {
            it("returns empty validation errors") {
                val validBeverageWithoutId = BeverageWithoutId(validName)
                val validationErrors: ValidationErrors = BeverageWithoutIdValidator.validate(validBeverageWithoutId)

                validationErrors.shouldBeEmpty()
            }
        }

        describe("when BeverageWithoutId is invalid") {
            val emptyString = ""
            val blankString = "     "
            val oversizeString = "a".repeat(256)
            val validString = "valid-string"

            val blankMessage = "must not be blank"
            val oversizeMessage = "size must be between 1 and 255"

            listOf(
                row(
                    "when name is empty",
                    "name",
                    BeverageWithoutId(emptyString),
                    listOf(blankMessage, oversizeMessage)
                ),
                row(
                    "when name is blank",
                    "name",
                    BeverageWithoutId(blankString),
                    listOf(blankMessage)
                ),
                row(
                    "when name has more than 255 characters",
                    "name",
                    BeverageWithoutId(oversizeString),
                    listOf(oversizeMessage)
                ),
                row(
                    "when settings key is empty",
                    "settings<K>[$emptyString].<map key>",
                    BeverageWithoutId(validName, mapOf(emptyString to "value")),
                    listOf(blankMessage, oversizeMessage)
                ),
                row(
                    "when settings key is blank",
                    "settings<K>[$blankString].<map key>",
                    BeverageWithoutId(validName, mapOf(blankString to validString)),
                    listOf(blankMessage)
                ),
                row(
                    "when settings key has more than 255 characters",
                    "settings<K>[$oversizeString].<map key>",
                    BeverageWithoutId(validName, mapOf(oversizeString to validString)),
                    listOf(oversizeMessage)
                ),
                row(
                    "when settings value is empty",
                    "settings[$validString].<map value>",
                    BeverageWithoutId(validName, mapOf(validString to emptyString)),
                    listOf(blankMessage, oversizeMessage)
                ),
                row(
                    "when settings value is blank",
                    "settings[$validString].<map value>",
                    BeverageWithoutId(validName, mapOf(validString to blankString)),
                    listOf(blankMessage)
                ),
                row(
                    "when settings value has more than 255 characters",
                    "settings[$validString].<map value>",
                    BeverageWithoutId(validName, mapOf(validString to oversizeString)),
                    listOf(oversizeMessage)
                ),
                row(
                    "when settings has more than 5 elements",
                    "settings",
                    BeverageWithoutId(
                        validName,
                        listOf("1", "2", "3", "4", "5", "6").associateWith { it }
                    ),
                    listOf("size must be between 0 and 5")
                )
            ).map { (
                        context: String,
                        field: String,
                        invalidBeverageWithoutId: BeverageWithoutId,
                        expectedErrors: List<String>
                    ) ->
                describe(context) {
                    it("returns error messages: $expectedErrors") {
                        val validationErrors: ValidationErrors = BeverageWithoutIdValidator
                            .validate(invalidBeverageWithoutId)

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
