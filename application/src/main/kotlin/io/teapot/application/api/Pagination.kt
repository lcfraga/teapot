package io.teapot.application.api

import io.teapot.usecase.pagination.FoundPage
import io.teapot.usecase.pagination.RequestedPage

data class PaginationParameters(val page: Int = DEFAULT_PAGE, val size: Int = DEFAULT_SIZE) {
    companion object {
        const val DEFAULT_PAGE = 1
        const val DEFAULT_SIZE = 20
    }
}

fun PaginationParameters.toRequestedPage() = RequestedPage(page, size)

data class PaginatedResponse<T>(
    val number: Int,
    val size: Int,
    val content: List<T>,
    val totalElements: Long,
    val totalPages: Int
)

fun <T, R> FoundPage<T>.toPaginatedResponse(transform: (T) -> R) = PaginatedResponse(
    number,
    size,
    content.map(transform),
    totalElements,
    totalPages
)
