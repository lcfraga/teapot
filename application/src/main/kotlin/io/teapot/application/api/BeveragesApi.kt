package io.teapot.application.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

data class CreateBeverageRequest(val name: String, val settings: Map<String, String> = emptyMap())

typealias UpdateBeverageRequest = CreateBeverageRequest

data class BeverageResponseBody(val id: String, val name: String, val settings: Map<String, String> = emptyMap())

@RequestMapping("/beverages")
@Tag(name = "Beverages", description = "Create, update, find and delete beverages")
interface BeveragesApi {
    @Operation(summary = "Find all beverages")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Beverages found",
                content = [
                    Content(
                        mediaType = "application/json",
                        array = ArraySchema(schema = Schema(implementation = BeverageResponseBody::class))
                    )
                ]
            )
        ]
    )
    @GetMapping(produces = ["application/json"])
    fun findAll(): List<BeverageResponseBody>

    @Operation(summary = "Find beverage by ID")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Beverage found",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = BeverageResponseBody::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "404", description = "Beverage not found", content = [Content()])
        ]
    )
    @GetMapping("/{id}", produces = ["application/json"])
    fun findById(
        @PathVariable("id") id: String
    ): ResponseEntity<Any>

    @Operation(summary = "Create beverage")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Beverage created",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = BeverageResponseBody::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "400", description = "Beverage is invalid", content = [Content()]),
            ApiResponse(responseCode = "409", description = "Beverage name is taken", content = [Content()])
        ]
    )
    @PostMapping(produces = ["application/json"])
    fun create(
        @RequestBody createUserRequest: CreateBeverageRequest
    ): ResponseEntity<Any>

    @Operation(summary = "Update beverage")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Beverage updated",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = BeverageResponseBody::class)
                    )
                ]
            ),
            ApiResponse(responseCode = "400", description = "Beverage is invalid", content = [Content()]),
            ApiResponse(responseCode = "404", description = "Beverage not found", content = [Content()]),
            ApiResponse(responseCode = "409", description = "Beverage name is taken", content = [Content()])
        ]
    )
    @PutMapping("/{id}", produces = ["application/json"])
    fun update(
        @PathVariable("id") id: String,
        @RequestBody updateUserRequest: UpdateBeverageRequest
    ): ResponseEntity<Any>

    @Operation(summary = "Delete beverage by ID")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Beverage deleted",
                content = [Content()]
            ),
            ApiResponse(responseCode = "404", description = "Beverage not found", content = [Content()])
        ]
    )
    @DeleteMapping("/{id}")
    fun deleteById(
        @PathVariable("id") id: String
    ): ResponseEntity<Any>
}
