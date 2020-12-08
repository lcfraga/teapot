package io.teapot.usecase.beverages

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.teapot.domain.entity.Beverage
import io.teapot.usecase.beverages.port.FindAllBeveragesPort

class FindAllBeveragesTest : DescribeSpec({
    val findAllBeveragesPort: FindAllBeveragesPort = mockk()
    val findAllBeverage = FindAllBeverages(findAllBeveragesPort)

    describe("findAll") {
        listOf(
            row(
                "when there are no beverages",
                emptyList()
            ),
            row(
                "when there are beverages",
                listOf(
                    Beverage("id-1", "Beverage 1"),
                    Beverage("id-2", "Beverage 2"),
                    Beverage("id-3", "Beverage 3")
                )
            )
        ).map { (
                    context: String,
                    beverages: List<Beverage>
                ) ->
            describe(context) {
                it("returns FindAllBeveragesResult.Found") {
                    every { findAllBeveragesPort.findAll() } returns beverages

                    findAllBeverage.findAll() shouldBe FindAllBeveragesResult.Found(beverages)

                    verify { findAllBeveragesPort.findAll() }
                    confirmVerified(findAllBeveragesPort)
                }
            }
        }
    }
})
