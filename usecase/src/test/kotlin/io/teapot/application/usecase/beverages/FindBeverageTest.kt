package io.teapot.application.usecase.beverages

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.teapot.domain.entity.Beverage
import io.teapot.usecase.beverages.FindBeverage
import io.teapot.usecase.beverages.FindBeverageResult
import io.teapot.usecase.beverages.port.FindBeveragePort
import java.util.Optional

class FindBeverageTest : DescribeSpec({

    val findBeveragePort: FindBeveragePort = mockk()
    val findBeverage = FindBeverage(findBeveragePort)

    describe("findById") {
        val beverageId = "beverage-id"

        describe("when beverage exists") {
            val beverage = Beverage(beverageId, "Test beverage")

            every { findBeveragePort.findById(beverageId) } returns Optional.of(beverage)

            it("returns FindBeverageResult.Found") {
                findBeverage.findById(beverageId) shouldBe FindBeverageResult.Found(beverage)

                verify { findBeveragePort.findById(beverageId) }
                confirmVerified(findBeveragePort)
            }
        }

        describe("when beverage does not exist") {
            every { findBeveragePort.findById(beverageId) } returns Optional.empty()

            it("returns FindBeverageResult.NotFound") {
                findBeverage.findById(beverageId) shouldBe FindBeverageResult.NotFound

                verify { findBeveragePort.findById(beverageId) }
                confirmVerified(findBeveragePort)
            }
        }
    }
})
