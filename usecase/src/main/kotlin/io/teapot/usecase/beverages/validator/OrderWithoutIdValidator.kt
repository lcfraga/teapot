package io.teapot.usecase.beverages.validator

import io.teapot.domain.entity.OrderWithoutId
import io.teapot.usecase.validation.EntityValidator

object OrderWithoutIdValidator : EntityValidator<OrderWithoutId>()
