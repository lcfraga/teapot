package io.teapot.usecase.beverages

import io.teapot.domain.entity.Beverage
import io.teapot.usecase.beverages.port.FindAllBeveragesPort
import io.teapot.usecase.pagination.FoundPage
import io.teapot.usecase.pagination.RequestedPage

sealed class FindAllBeveragesResult {
    data class Found(val beverages: FoundPage<Beverage>) : FindAllBeveragesResult()
}

class FindAllBeverages(private val findAllBeveragesPort: FindAllBeveragesPort) {

    fun findAll(requestedPage: RequestedPage): FindAllBeveragesResult {
        return FindAllBeveragesResult.Found(
            findAllBeveragesPort.findAll(requestedPage.safeCopy())
        )
    }
}
