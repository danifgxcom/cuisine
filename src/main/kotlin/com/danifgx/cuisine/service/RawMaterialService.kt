package com.danifgx.cuisine.service

import com.danifgx.cuisine.repository.RawMaterialRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class RawMaterialService(private val repository: RawMaterialRepository) {

    fun getAllRawMaterialsWithEnglishDescription(language: String): List<RawMaterialWithTranslation> {
        val rawMaterials = repository.findAll()

        return rawMaterials.mapNotNull { rawMaterial ->
            val translation = rawMaterial.translations.find { it.language == language }
            translation?.let {
                RawMaterialWithTranslation(
                    id = rawMaterial.id,
                    type = rawMaterial.type,
                    nutritionalGroup = rawMaterial.nutritionalGroup,
                    kcalPer100Gr = rawMaterial.kcalPer100Gr,
                    unit = rawMaterial.unit,
                    name = it.name,
                    description = it.description
                )
            }
        }
    }
}

data class RawMaterialWithTranslation(
    val id: UUID,
    val type: String,
    val nutritionalGroup: String,
    val kcalPer100Gr: Double,
    val unit: String,
    val name: String?,
    val description: String?
)