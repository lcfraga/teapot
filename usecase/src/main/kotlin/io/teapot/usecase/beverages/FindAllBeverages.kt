package io.teapot.usecase.beverages

import io.teapot.domain.entity.Beverage
import io.teapot.usecase.beverages.port.FindAllBeveragesPort

sealed class FindAllBeveragesResult {
    data class Found(val beverages: List<Beverage>) : FindAllBeveragesResult()
}

class FindAllBeverages(private val findAllBeveragesPort: FindAllBeveragesPort) {

    fun findAll(): FindAllBeveragesResult {
        return FindAllBeveragesResult.Found(findAllBeveragesPort.findAll())
    }
}
