package io.teapot.usecase.beverages

import io.teapot.domain.entity.Beverage
import io.teapot.domain.entity.BeverageWithoutId
import io.teapot.usecase.beverages.port.FindBeveragePort
import io.teapot.usecase.beverages.port.SaveBeveragePort
import io.teapot.usecase.beverages.validator.BeverageWithoutIdValidator
import io.teapot.usecase.validation.ValidationErrors

sealed class UpdateBeverageResult {
    data class Updated(val beverage: Beverage) : UpdateBeverageResult()
    data class Invalid(val errors: ValidationErrors) : UpdateBeverageResult()
    data class Conflict(val message: String) : UpdateBeverageResult()
    object NotFound : UpdateBeverageResult()
}

class UpdateBeverage(private val findBeveragePort: FindBeveragePort, private val saveBeveragePort: SaveBeveragePort) {

    fun update(id: String, beverageWithoutId: BeverageWithoutId): UpdateBeverageResult {
        val validationErrors: ValidationErrors = BeverageWithoutIdValidator.validate(beverageWithoutId)

        if (validationErrors.isNotEmpty()) {
            return UpdateBeverageResult.Invalid(validationErrors)
        }

        if (!findBeveragePort.findById(id).isPresent) {
            return UpdateBeverageResult.NotFound
        }

        if (isNameTakenByAnotherId(beverageWithoutId.name, id)) {
            return UpdateBeverageResult.Conflict("name ${beverageWithoutId.name} is taken")
        }

        return UpdateBeverageResult.Updated(
            saveBeveragePort.save(
                Beverage(
                    id,
                    beverageWithoutId.name,
                    beverageWithoutId.settings
                )
            )
        )
    }

    private fun isNameTakenByAnotherId(name: String, id: String): Boolean {
        return findBeveragePort
            .findByNameIgnoringCase(name)
            .map { it.id != id }
            .orElse(false)
    }
}
