package io.teapot.usecase.pagination

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.data.row
import io.kotest.matchers.shouldBe

class RequestedPageTest : DescribeSpec({
    describe("safeCopy") {
        listOf(
            row(
                "when page is less than minimum page",
                RequestedPage(RequestedPage.MIN_PAGE - 1),
                "copied page is set to minimum page",
                RequestedPage()
            ),
            row(
                "when page is greater than maximum page",
                RequestedPage(RequestedPage.MAX_PAGE + 1),
                "copied page is set to maximum page",
                RequestedPage(RequestedPage.MAX_PAGE)
            ),
            row(
                "when size is less than minimum size",
                RequestedPage(RequestedPage.MIN_PAGE, RequestedPage.MIN_SIZE - 1),
                "copied size is set to minimum size",
                RequestedPage(RequestedPage.MIN_PAGE, RequestedPage.MIN_SIZE)
            ),
            row(
                "when size is greater than maximum size",
                RequestedPage(RequestedPage.MIN_PAGE, RequestedPage.MAX_SIZE + 1),
                "copied size is set to maximum size",
                RequestedPage(RequestedPage.MIN_PAGE, RequestedPage.MAX_SIZE)
            ),
        ).map { (
            context: String,
            originalRequestedPage: RequestedPage,
            itMessage: String,
            safeCopyRequestPage: RequestedPage
        ) ->
            describe(context) {
                it(itMessage) {
                    originalRequestedPage.safeCopy() shouldBe safeCopyRequestPage
                }
            }
        }
    }
})
