package io.teapot.usecase.pagination

data class RequestedPage(val page: Int = MIN_PAGE, val size: Int = DEFAULT_SIZE) {

    companion object {
        const val MIN_PAGE = 1
        const val MAX_PAGE = 10000
        const val DEFAULT_SIZE = 20
        const val MIN_SIZE = 1
        const val MAX_SIZE = 100
    }

    fun safeCopy(): RequestedPage {
        val safePage = when {
            page < MIN_PAGE -> MIN_PAGE
            page > MAX_PAGE -> MAX_PAGE
            else -> page
        }

        val safeSize = when {
            size < MIN_SIZE -> MIN_SIZE
            size > MAX_SIZE -> MAX_SIZE
            else -> size
        }

        return RequestedPage(safePage, safeSize)
    }
}
