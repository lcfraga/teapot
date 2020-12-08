package io.teapot.usecase.beverages.port

import io.teapot.domain.entity.Beverage
import io.teapot.usecase.pagination.FoundPage
import io.teapot.usecase.pagination.RequestedPage

interface FindAllBeveragesPort {
    fun findAll(requestedPage: RequestedPage): FoundPage<Beverage>
}
