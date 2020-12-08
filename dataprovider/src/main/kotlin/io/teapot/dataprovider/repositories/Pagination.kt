package io.teapot.dataprovider.repositories

import io.teapot.usecase.pagination.FoundPage
import org.springframework.data.domain.Page

fun <T, R> Page<T>.toFoundPage(transform: (T) -> R) = FoundPage(
    number + 1, // Spring uses 0-based pagination.
    size,
    content.map(transform),
    totalElements,
    totalPages
)
