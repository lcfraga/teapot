package io.teapot.usecase.pagination

data class FoundPage<T>(
    val number: Int,
    val size: Int,
    val content: List<T>,
    val totalElements: Long,
    val totalPages: Int
)
