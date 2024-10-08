package com.danifgx.cuisine.controller

import com.danifgx.cuisine.log.logger
import com.danifgx.cuisine.model.RawMaterial
import com.danifgx.cuisine.service.RawMaterialService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.constraints.NotNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/materials")
class RawMaterialController(private val rawMaterialService: RawMaterialService) {

    private val logger = logger()

    @Operation(summary = "Get all raw materials with their description in the specified language")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully retrieved the materials with descriptions"),
        ApiResponse(responseCode = "400", description = "Invalid language parameter", content = [Content()]),
        ApiResponse(responseCode = "500", description = "Internal server error", content = [Content()])
    ])
    @GetMapping("/with-description")
    fun listWithDescription(
        @Parameter(description = "Language code (e.g., 'en' for English, 'es' for Spanish)")
        @RequestParam @NotNull language: String
    ): ResponseEntity<List<RawMaterial>> {
        logger.info("Calling materials with description in language: $language")
        val materials = rawMaterialService.getAllRawMaterialsWithDescription(language)
        return ResponseEntity.ok(materials)
    }

    @Operation(summary = "Get a list of all raw materials")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully retrieved the list of raw materials"),
        ApiResponse(responseCode = "500", description = "Internal server error", content = [Content()])
    ])
    @GetMapping
    fun listAllMaterials(): ResponseEntity<List<RawMaterial>> {
        val materials = rawMaterialService.getAllRawMaterials()
        return ResponseEntity.ok(materials)
    }

    @Operation(summary = "Get a specific raw material by its ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully retrieved the raw material"),
        ApiResponse(responseCode = "404", description = "Raw material not found", content = [Content()]),
        ApiResponse(responseCode = "500", description = "Internal server error", content = [Content()])
    ])
    @GetMapping("/{id}")
    fun getMaterialById(
        @Parameter(description = "UUID of the raw material") @PathVariable id: UUID
    ): ResponseEntity<RawMaterial> {
        val material = rawMaterialService.getRawMaterialById(id)
        return if (material != null) {
            ResponseEntity.ok(material)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @Operation(summary = "Create a new raw material")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Successfully created the raw material"),
        ApiResponse(responseCode = "400", description = "Invalid input", content = [Content()]),
        ApiResponse(responseCode = "500", description = "Internal server error", content = [Content()])
    ])
    @PostMapping
    fun createRawMaterial(
        @Parameter(description = "The raw material object to be created") @RequestBody rawMaterial: RawMaterial
    ): ResponseEntity<RawMaterial> {
        val createdMaterial = rawMaterialService.createRawMaterial(rawMaterial)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMaterial)
    }

    @Operation(summary = "Update an existing raw material by its ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully updated the raw material"),
        ApiResponse(responseCode = "404", description = "Raw material not found", content = [Content()]),
        ApiResponse(responseCode = "400", description = "Invalid input", content = [Content()]),
        ApiResponse(responseCode = "500", description = "Internal server error", content = [Content()])
    ])
    @PutMapping("/{id}")
    fun updateRawMaterial(
        @Parameter(description = "UUID of the raw material") @PathVariable id: UUID,
        @Parameter(description = "Updated raw material object") @RequestBody updatedMaterial: RawMaterial
    ): ResponseEntity<RawMaterial> {
        val updated = rawMaterialService.updateRawMaterial(id, updatedMaterial)
        return ResponseEntity.ok(updated)
    }

    @Operation(summary = "Delete a raw material by its ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Successfully deleted the raw material"),
        ApiResponse(responseCode = "404", description = "Raw material not found", content = [Content()]),
        ApiResponse(responseCode = "500", description = "Internal server error", content = [Content()])
    ])
    @DeleteMapping("/{id}")
    fun deleteRawMaterial(
        @Parameter(description = "UUID of the raw material") @PathVariable id: UUID
    ): ResponseEntity<Unit> {
        val deleted = rawMaterialService.deleteRawMaterial(id)
        return if (deleted) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
