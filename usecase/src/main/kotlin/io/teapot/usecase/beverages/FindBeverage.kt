package io.teapot.usecase.beverages

import io.teapot.domain.entity.Beverage
import io.teapot.usecase.beverages.port.FindBeveragePort

sealed class FindBeverageResult {
    data class Found(val beverage: Beverage) : FindBeverageResult()
    object NotFound : FindBeverageResult()
}

class FindBeverage(private val findBeveragePort: FindBeveragePort) {

    fun findById(id: String): FindBeverageResult {
        return findBeveragePort.findById(id)
            .map { found(it) }
            .orElseGet { FindBeverageResult.NotFound }
    }

    private fun found(beverage: Beverage): FindBeverageResult {
        return FindBeverageResult.Found(beverage)
    }
}
