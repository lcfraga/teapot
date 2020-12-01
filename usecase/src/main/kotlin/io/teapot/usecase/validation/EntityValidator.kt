package io.teapot.usecase.validation

import javax.validation.ConstraintViolation
import javax.validation.Validation
import javax.validation.Validator

typealias ValidationErrors = MutableMap<String, MutableList<String>>

open class EntityValidator<T> {
    private val validator: Validator = Validation.buildDefaultValidatorFactory().validator

    fun validate(entity: T): ValidationErrors {
        val constraintViolations: Set<ConstraintViolation<T>> = validator.validate(entity)

        val validationErrors: ValidationErrors = mutableMapOf()

        constraintViolations.forEach {
            validationErrors
                .getOrPut(it.propertyPath.toString()) { mutableListOf() }
                .add(it.message)
        }

        return validationErrors
    }
}
