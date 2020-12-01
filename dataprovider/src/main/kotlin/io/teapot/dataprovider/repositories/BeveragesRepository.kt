package io.teapot.dataprovider.repositories

import io.teapot.dataprovider.entities.BeverageEntity
import io.teapot.domain.entity.Beverage
import io.teapot.usecase.beverages.port.DeleteBeveragePort
import io.teapot.usecase.beverages.port.FindAllBeveragesPort
import io.teapot.usecase.beverages.port.FindBeveragePort
import io.teapot.usecase.beverages.port.SaveBeveragePort
import java.util.Optional

private fun BeverageEntity.toBeverage() = Beverage(
    id,
    name,
    settings
)

private fun Beverage.toBeverageEntity() = BeverageEntity(
    id,
    name,
    settings
)

class BeveragesRepository(
    private val jpaBeveragesRepository: JpaBeveragesRepository
) : FindAllBeveragesPort, FindBeveragePort, SaveBeveragePort, DeleteBeveragePort {

    override fun findAll(): List<Beverage> {
        return jpaBeveragesRepository
            .findAll()
            .map(BeverageEntity::toBeverage)
    }

    override fun findById(id: String): Optional<Beverage> {
        return jpaBeveragesRepository
            .findById(id)
            .map(BeverageEntity::toBeverage)
    }

    override fun findByNameIgnoringCase(name: String): Optional<Beverage> {
        return jpaBeveragesRepository
            .findByNameIgnoreCase(name)
            .map(BeverageEntity::toBeverage)
    }

    override fun save(beverage: Beverage): Beverage {
        return jpaBeveragesRepository
            .save(beverage.toBeverageEntity())
            .toBeverage()
    }

    override fun deleteById(id: String) {
        jpaBeveragesRepository.deleteById(id)
    }
}
