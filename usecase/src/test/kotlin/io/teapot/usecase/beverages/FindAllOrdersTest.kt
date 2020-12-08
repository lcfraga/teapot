package io.teapot.usecase.beverages

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.teapot.domain.entity.Order
import io.teapot.usecase.beverages.port.FindAllOrdersPort
import io.teapot.usecase.pagination.FoundPage
import io.teapot.usecase.pagination.RequestedPage

class FindAllOrdersTest : DescribeSpec({
    val findAllOrdersPort: FindAllOrdersPort = mockk()
    val findAllOrder = FindAllOrders(findAllOrdersPort)

    val unsafeRequestedPage = RequestedPage(-1)
    val safeRequestedPage = unsafeRequestedPage.safeCopy()

    val foundPage = FoundPage<Order>(safeRequestedPage.page, safeRequestedPage.size, emptyList(), 0, 0)

    describe("findAll") {
        it("returns FindAllOrdersResult.Found") {
            every { findAllOrdersPort.findAll(safeRequestedPage) } returns foundPage

            findAllOrder.findAll(unsafeRequestedPage) shouldBe FindAllOrdersResult.Found(foundPage)

            verify { findAllOrdersPort.findAll(safeRequestedPage) }
            confirmVerified(findAllOrdersPort)
        }
    }
})
