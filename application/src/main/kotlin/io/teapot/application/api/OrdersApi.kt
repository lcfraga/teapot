package io.teapot.application.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import java.time.Instant

data class OrderBeverageRequest(val beverage: String, val username: String, val size: String)

data class OrderResponseBody(
    val id: String,
    val beverage: String,
    val settings: Map<String, String>,
    val username: String,
    val size: String,
    val createdAt: Instant,
    val served: Boolean,
    val servedAt: Instant?
)

@RequestMapping("/orders")
@Tag(name = "Orders", description = "Create and find beverage orders")
interface OrdersApi {
    @Operation(summary = "Find all orders (paginated)")
    @ApiResponses(
        value = [ApiResponse(responseCode = "200", description = "Orders found")]
    )
    @Parameters(
        Parameter(
            `in` = ParameterIn.QUERY,
            name = "page",
            schema = Schema(type = "integer", defaultValue = PaginationParameters.DEFAULT_PAGE.toString())
        ),
        Parameter(
            `in` = ParameterIn.QUERY,
            name = "size",
            schema = Schema(type = "integer", defaultValue = PaginationParameters.DEFAULT_SIZE.toString())
        )
    )
    @GetMapping(produces = ["application/json"])
    fun findAll(
        @Parameter(hidden = true) paginationParameters: PaginationParameters
    ): PaginatedResponse<OrderResponseBody>

    @Operation(summary = "Find order by ID")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Order found",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = OrderResponseBody::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "404", description = "Order not found", content = [Content()])
        ]
    )
    @GetMapping("/{id}", produces = ["application/json"])
    fun findById(
        @PathVariable("id") id: String
    ): ResponseEntity<Any>

    @Operation(summary = "Order beverage")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Beverage ordered",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = OrderResponseBody::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "400", description = "Order is invalid", content = [Content()]),
            ApiResponse(responseCode = "404", description = "Beverage does not exist", content = [Content()]),
            ApiResponse(responseCode = "418", description = "Teapot cannot brew beverage", content = [Content()]),
            ApiResponse(responseCode = "429", description = "Teapot does not have enough water", content = [Content()])
        ]
    )
    @PostMapping(produces = ["application/json"])
    fun create(
        @RequestBody orderBeverageRequest: OrderBeverageRequest
    ): ResponseEntity<Any>
}
