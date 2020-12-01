package io.teapot.usecase.beverages.port

import io.teapot.domain.entity.Beverage

interface SaveBeveragePort {
    fun save(beverage: Beverage): Beverage
}
