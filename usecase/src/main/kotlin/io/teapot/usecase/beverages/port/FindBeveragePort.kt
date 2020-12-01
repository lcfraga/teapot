package io.teapot.usecase.beverages.port

import io.teapot.domain.entity.Beverage
import java.util.Optional

interface FindBeveragePort {
    fun findByNameIgnoringCase(name: String): Optional<Beverage>
}
