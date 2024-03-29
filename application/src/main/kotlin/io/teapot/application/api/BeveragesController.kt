package io.teapot.application.api

import io.teapot.domain.entity.Beverage
import io.teapot.domain.entity.BeverageWithoutId
import io.teapot.usecase.beverages.CreateBeverage
import io.teapot.usecase.beverages.CreateBeverageResult
import io.teapot.usecase.beverages.DeleteBeverage
import io.teapot.usecase.beverages.DeleteBeverageResult
import io.teapot.usecase.beverages.FindAllBeverages
import io.teapot.usecase.beverages.FindAllBeveragesResult
import io.teapot.usecase.beverages.FindBeverage
import io.teapot.usecase.beverages.FindBeverageResult
import io.teapot.usecase.beverages.UpdateBeverage
import io.teapot.usecase.beverages.UpdateBeverageResult
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RestController

private fun CreateBeverageRequest.toBeverageWithoutId() = BeverageWithoutId(
    name,
    settings
)

private fun Beverage.toBeverageResponseBody() = BeverageResponseBody(
    id,
    name,
    settings
)

@RestController
class BeveragesController(
    private val findAllBeverages: FindAllBeverages,
    private val findBeverage: FindBeverage,
    private val createBeverage: CreateBeverage,
    private val updateBeverage: UpdateBeverage,
    private val deleteBeverage: DeleteBeverage
) : BeveragesApi {

    @Transactional(readOnly = true)
    override fun findAll(paginationParameters: PaginationParameters): PaginatedResponse<BeverageResponseBody> {
        return when (val findAllBeveragesResult = findAllBeverages.findAll(paginationParameters.toRequestedPage())) {
            is FindAllBeveragesResult.Found ->
                findAllBeveragesResult.beverages
                    .toPaginatedResponse(Beverage::toBeverageResponseBody)
        }
    }

    @Transactional(readOnly = true)
    override fun findById(id: String): ResponseEntity<Any> {
        return when (val findBeverageResult = findBeverage.findById(id)) {
            is FindBeverageResult.Found ->
                ResponseEntity
                    .status(HttpStatus.OK)
                    .body(findBeverageResult.beverage.toBeverageResponseBody())
            is FindBeverageResult.NotFound ->
                ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build()
        }
    }

    @Transactional
    override fun create(createUserRequest: CreateBeverageRequest): ResponseEntity<Any> {
        return when (val createBeverageResult = createBeverage.create(createUserRequest.toBeverageWithoutId())) {
            is CreateBeverageResult.Created ->
                ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(createBeverageResult.beverage.toBeverageResponseBody())
            is CreateBeverageResult.Invalid ->
                ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(createBeverageResult.errors)
            is CreateBeverageResult.Conflict ->
                ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build()
        }
    }

    @Transactional
    override fun update(id: String, updateUserRequest: UpdateBeverageRequest): ResponseEntity<Any> {
        return when (val updateBeverageResult = updateBeverage.update(id, updateUserRequest.toBeverageWithoutId())) {
            is UpdateBeverageResult.Updated ->
                ResponseEntity
                    .status(HttpStatus.OK)
                    .body(updateBeverageResult.beverage.toBeverageResponseBody())
            is UpdateBeverageResult.Invalid ->
                ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(updateBeverageResult.errors)
            is UpdateBeverageResult.Conflict ->
                ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build()
            is UpdateBeverageResult.NotFound ->
                ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build()
        }
    }

    @Transactional
    override fun deleteById(id: String): ResponseEntity<Any> {
        return when (deleteBeverage.deleteById(id)) {
            is DeleteBeverageResult.Deleted ->
                ResponseEntity
                    .status(HttpStatus.OK)
                    .build()
            is DeleteBeverageResult.NotFound ->
                ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build()
        }
    }
}
