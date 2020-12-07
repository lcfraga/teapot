package io.teapot.application.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
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
