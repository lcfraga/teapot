package io.teapot.usecase.beverages.port

import io.teapot.domain.entity.Beverage

interface FindAllBeveragesPort {
    fun findAll(): List<Beverage>
}
