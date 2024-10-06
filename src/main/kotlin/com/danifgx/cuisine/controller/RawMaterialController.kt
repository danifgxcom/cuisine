package com.danifgx.cuisine.controller

import com.danifgx.cuisine.log.logger
import com.danifgx.cuisine.model.RawMaterial
import com.danifgx.cuisine.service.RawMaterialService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/materials")
class RawMaterialController(private val rawMaterialService: RawMaterialService) {

    private val logger = logger()

    @GetMapping("/with-description")
    fun listWithDescription(@RequestParam language: String): ResponseEntity<List<RawMaterial>> {
        logger.info("Calling materials with description in language: $language")
        val materials = rawMaterialService.getAllRawMaterialsWithDescription(language)
        return ResponseEntity.ok(materials)
    }

    @GetMapping
    fun listAllMaterials(): ResponseEntity<List<RawMaterial>> {
        val materials = rawMaterialService.getAllRawMaterials()
        return ResponseEntity.ok(materials)
    }

    @GetMapping("/{id}")
    fun getMaterialById(@PathVariable id: UUID): ResponseEntity<RawMaterial> {
        val material = rawMaterialService.getRawMaterialById(id)
        return if (material != null) {
            ResponseEntity.ok(material)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    fun createRawMaterial(@RequestBody rawMaterial: RawMaterial): ResponseEntity<RawMaterial> {
        val createdMaterial = rawMaterialService.createRawMaterial(rawMaterial)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMaterial)
    }

    @PutMapping("/{id}")
    fun updateRawMaterial(@PathVariable id: UUID, @RequestBody updatedMaterial: RawMaterial): ResponseEntity<RawMaterial> {
        val updated = rawMaterialService.updateRawMaterial(id, updatedMaterial)
        return ResponseEntity.ok(updated)
    }

    @DeleteMapping("/{id}")
    fun deleteRawMaterial(@PathVariable id: UUID): ResponseEntity<Unit> {
        val deleted = rawMaterialService.deleteRawMaterial(id)
        return if (deleted) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}