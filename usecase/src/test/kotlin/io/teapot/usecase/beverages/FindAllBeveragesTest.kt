package io.teapot.usecase.beverages

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.teapot.domain.entity.Beverage
import io.teapot.usecase.beverages.port.FindAllBeveragesPort
import io.teapot.usecase.pagination.FoundPage
import io.teapot.usecase.pagination.RequestedPage

class FindAllBeveragesTest : DescribeSpec({
    val findAllBeveragesPort: FindAllBeveragesPort = mockk()
    val findAllBeverage = FindAllBeverages(findAllBeveragesPort)

    val unsafeRequestedPage = RequestedPage(-1)
    val safeRequestedPage = unsafeRequestedPage.safeCopy()

    val foundPage = FoundPage<Beverage>(safeRequestedPage.page, safeRequestedPage.size, emptyList(), 0, 0)

    describe("findAll") {
        it("returns FindAllBeveragesResult.Found") {
            every { findAllBeveragesPort.findAll(safeRequestedPage) } returns foundPage

            findAllBeverage.findAll(unsafeRequestedPage) shouldBe FindAllBeveragesResult.Found(foundPage)

            verify { findAllBeveragesPort.findAll(safeRequestedPage) }
            confirmVerified(findAllBeveragesPort)
        }
    }
})
