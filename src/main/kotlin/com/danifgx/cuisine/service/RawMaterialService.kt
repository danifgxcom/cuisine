package com.danifgx.cuisine.service

import com.danifgx.cuisine.log.logger
import com.danifgx.cuisine.model.RawMaterial
import com.danifgx.cuisine.repository.RawMaterialRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.*

@Service
class RawMaterialService(private val rawMaterialRepository: RawMaterialRepository) {

    private val logger = logger()

    fun getAllRawMaterials(): List<RawMaterial> {
        return rawMaterialRepository.findAll()
    }

    fun getAllRawMaterialsWithDescription(language: String): List<RawMaterial> {
        return rawMaterialRepository.findAll().filter { rawMaterial ->
            rawMaterial.translations.any { it.language == language }
        }.map { rawMaterial ->
            rawMaterial.copy(translations = rawMaterial.translations.filter { it.language == language })
        }
    }

    fun getRawMaterialById(id: UUID): RawMaterial? {
        return rawMaterialRepository.findById(id).orElse(null)
    }

    fun createRawMaterial(rawMaterial: RawMaterial): RawMaterial {
        return rawMaterialRepository.save(rawMaterial)
    }

    fun updateRawMaterial(id: UUID, updatedMaterial: RawMaterial): RawMaterial {
        val existingMaterial = rawMaterialRepository.findById(id).orElseThrow {
            throw RawMaterialNotFoundException("Raw material with ID $id not found")
        }
        val updatedRawMaterial = existingMaterial.copy(
            type = updatedMaterial.type,
            nutritionalGroup = updatedMaterial.nutritionalGroup,
            kcalPer100Gr = updatedMaterial.kcalPer100Gr,
            unit = updatedMaterial.unit,
            translations = updatedMaterial.translations
        )
        return rawMaterialRepository.save(updatedRawMaterial)
    }

    fun deleteRawMaterial(id: UUID): Boolean {
        rawMaterialRepository.findById(id).ifPresent { rawMaterialRepository.delete(it) }
        return rawMaterialRepository.existsById(id).not()
    }

    fun findRawMaterialIdsByNames(names: List<String>): List<UUID> {
        val normalizedNames = names.map { it.lowercase(Locale.getDefault()) }

        val result = rawMaterialRepository.findAll().filter { rawMaterial ->
            rawMaterial.translations.any { translation ->
                val translationNameLowercase = translation.name.lowercase(Locale.getDefault())
                normalizedNames.any { name ->
                    val match = translationNameLowercase.contains(name)
                    logger.info("Checking translation name: $translationNameLowercase against names: $normalizedNames")
                    match
                }
            }
        }.map { it.id }

        return result
    }
}

@ResponseStatus(HttpStatus.NOT_FOUND)
class RawMaterialNotFoundException(message: String) : RuntimeException(message)