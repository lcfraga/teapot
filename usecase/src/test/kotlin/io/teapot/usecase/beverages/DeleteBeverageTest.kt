package io.teapot.usecase.beverages

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.Called
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import io.teapot.domain.entity.Beverage
import io.teapot.usecase.beverages.port.DeleteBeveragePort
import io.teapot.usecase.beverages.port.FindBeveragePort
import java.util.Optional

class DeleteBeverageTest : DescribeSpec({

    val findBeveragePort: FindBeveragePort = mockk()
    val deleteBeveragePort: DeleteBeveragePort = mockk()
    val deleteBeverage = DeleteBeverage(findBeveragePort, deleteBeveragePort)

    describe("deleteById") {
        val beverageId = "beverage-id"

        describe("when beverage does not exist") {
            it("returns DeleteBeverageResult.NotFound") {
                every { findBeveragePort.findById(beverageId) } returns Optional.empty()

                deleteBeverage.deleteById(beverageId) shouldBe DeleteBeverageResult.NotFound

                verify { findBeveragePort.findById(beverageId) }
                verify { deleteBeveragePort wasNot Called }

                confirmVerified(findBeveragePort)
            }
        }

        describe("when beverage exists") {
            val beverage = Beverage(beverageId, "Test beverage")

            it("returns DeleteBeverageResult.Deleted") {
                every { findBeveragePort.findById(beverageId) } returns Optional.of(beverage)
                justRun { deleteBeveragePort.deleteById(beverageId) }

                deleteBeverage.deleteById(beverageId) shouldBe DeleteBeverageResult.Deleted

                verifyOrder {
                    findBeveragePort.findById(beverageId)
                    deleteBeveragePort.deleteById(beverageId)
                }

                confirmVerified(findBeveragePort, deleteBeveragePort)
            }
        }
    }
})
