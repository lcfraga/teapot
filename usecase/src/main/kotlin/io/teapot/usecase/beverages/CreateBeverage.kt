package io.teapot.usecase.beverages

import io.teapot.domain.entity.Beverage
import io.teapot.domain.entity.BeverageWithoutId
import io.teapot.usecase.beverages.port.FindBeveragePort
import io.teapot.usecase.beverages.port.GenerateIdPort
import io.teapot.usecase.beverages.port.SaveBeveragePort
import io.teapot.usecase.beverages.validator.BeverageWithoutIdValidator
import io.teapot.usecase.validation.ValidationErrors

sealed class CreateBeverageResult {
    data class Created(val beverage: Beverage) : CreateBeverageResult()
    data class Invalid(val errors: ValidationErrors) : CreateBeverageResult()
    data class Conflict(val message: String) : CreateBeverageResult()
}

class CreateBeverage(
    private val findBeveragePort: FindBeveragePort,
    private val generateIdPort: GenerateIdPort,
    private val saveBeveragePort: SaveBeveragePort
) {

    fun create(beverageWithoutId: BeverageWithoutId): CreateBeverageResult {
        val validationErrors: ValidationErrors = BeverageWithoutIdValidator.validate(beverageWithoutId)

        if (validationErrors.isNotEmpty()) {
            return CreateBeverageResult.Invalid(validationErrors)
        }

        if (findBeveragePort.findByNameIgnoringCase(beverageWithoutId.name).isPresent) {
            return CreateBeverageResult.Conflict("name ${beverageWithoutId.name} is taken")
        }

        val createdBeverage: Beverage = saveBeveragePort.save(
            Beverage(
                generateIdPort.generate(),
                beverageWithoutId.name,
                beverageWithoutId.settings
            )
        )

        return CreateBeverageResult.Created(createdBeverage)
    }
}
