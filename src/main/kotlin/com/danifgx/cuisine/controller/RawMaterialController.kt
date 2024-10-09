package com.danifgx.cuisine.controller

import com.danifgx.cuisine.controller.hateoas.HateoasLinkHelper.addLinksToRawMaterialCollection
import com.danifgx.cuisine.controller.hateoas.HateoasLinkHelper.addSelfLinkToRawMaterialDTO
import com.danifgx.cuisine.log.logger
import com.danifgx.cuisine.model.RawMaterial
import com.danifgx.cuisine.model.RawMaterialDTO
import com.danifgx.cuisine.model.mapper.RawMaterialMapper
import com.danifgx.cuisine.service.RawMaterialService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.constraints.NotNull
import org.springframework.hateoas.CollectionModel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/materials")
class RawMaterialController(
    private val rawMaterialService: RawMaterialService,
    private val rawMaterialMapper: RawMaterialMapper
) {

    private val logger = logger()


    @Operation(summary = "Get all raw materials with their description in the specified language")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved the materials with descriptions"),
            ApiResponse(responseCode = "400", description = "Invalid language parameter", content = [Content()]),
            ApiResponse(responseCode = "500", description = "Internal server error", content = [Content()])
        ]
    )
    @GetMapping("/with-description")
    fun listWithDescription(
        @Parameter(description = "Language code (e.g., 'en' for English, 'es' for Spanish)")
        @RequestParam @NotNull language: String
    ): ResponseEntity<CollectionModel<RawMaterialDTO>> {
        logger.info("Calling materials with description in language: $language")
        val materials = rawMaterialService.getAllRawMaterialsWithDescription(language)
        val materialDTOs = materials.map { rawMaterialMapper.toDTO(it) }

        val collectionModel = addLinksToRawMaterialCollection(materialDTOs, language)
        return ResponseEntity.ok(collectionModel)
    }

    @Operation(summary = "Get a list of all raw materials")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved the list of raw materials"),
            ApiResponse(responseCode = "500", description = "Internal server error", content = [Content()])
        ]
    )
    @GetMapping
    fun listAllMaterials(): ResponseEntity<CollectionModel<RawMaterialDTO>> {
        val materials = rawMaterialService.getAllRawMaterials()
        val materialDTOs = materials.map { rawMaterialMapper.toDTO(it) }

        val collectionModel = addLinksToRawMaterialCollection(materialDTOs)
        return ResponseEntity.ok(collectionModel)
    }

    @Operation(summary = "Get a specific raw material by its ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully retrieved the raw material"),
            ApiResponse(responseCode = "404", description = "Raw material not found", content = [Content()]),
            ApiResponse(responseCode = "500", description = "Internal server error", content = [Content()])
        ]
    )
    @GetMapping("/{id}")
    fun getMaterialById(
        @Parameter(description = "UUID of the raw material") @PathVariable id: UUID
    ): ResponseEntity<RawMaterialDTO> {
        val material = rawMaterialService.getRawMaterialById(id) ?: return ResponseEntity.notFound().build()
        val materialDTO = rawMaterialMapper.toDTO(material)

        return ResponseEntity.ok(addSelfLinkToRawMaterialDTO(materialDTO))
    }

    @Operation(summary = "Create a new raw material")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Successfully created the raw material"),
            ApiResponse(responseCode = "400", description = "Invalid input", content = [Content()]),
            ApiResponse(responseCode = "500", description = "Internal server error", content = [Content()])
        ]
    )
    @PostMapping
    fun createRawMaterial(
        @Parameter(description = "The raw material object to be created") @RequestBody rawMaterial: RawMaterial
    ): ResponseEntity<RawMaterialDTO> {
        val createdMaterial = rawMaterialService.createRawMaterial(rawMaterial)
        val materialDTO = rawMaterialMapper.toDTO(createdMaterial)

        return ResponseEntity.status(HttpStatus.CREATED).body(addSelfLinkToRawMaterialDTO(materialDTO))
    }

    @Operation(summary = "Update an existing raw material by its ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully updated the raw material"),
            ApiResponse(responseCode = "404", description = "Raw material not found", content = [Content()]),
            ApiResponse(responseCode = "400", description = "Invalid input", content = [Content()]),
            ApiResponse(responseCode = "500", description = "Internal server error", content = [Content()])
        ]
    )
    @PutMapping("/{id}")
    fun updateRawMaterial(
        @Parameter(description = "UUID of the raw material") @PathVariable id: UUID,
        @Parameter(description = "Updated raw material object") @RequestBody updatedMaterial: RawMaterial
    ): ResponseEntity<RawMaterialDTO> {
        val updated = rawMaterialService.updateRawMaterial(id, updatedMaterial)
        val updatedDTO = rawMaterialMapper.toDTO(updated)

        return ResponseEntity.ok(addSelfLinkToRawMaterialDTO(updatedDTO))
    }

    @Operation(summary = "Delete a raw material by its ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Successfully deleted the raw material"),
            ApiResponse(responseCode = "404", description = "Raw material not found", content = [Content()]),
            ApiResponse(responseCode = "500", description = "Internal server error", content = [Content()])
        ]
    )
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
