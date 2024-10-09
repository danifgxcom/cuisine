package com.danifgx.cuisine.model

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.hateoas.RepresentationModel
import java.util.UUID

data class RawMaterialDTO(
    val id: UUID,
    val type: String,
    val nutritionalGroup: String,
    val kcalPer100Gr: Double,
    val unit: String,
    val translations: List<TranslationDTO>
) : RepresentationModel<RawMaterialDTO>()

data class TranslationDTO(
    val language: String,
    val name: String,
    val description: String
)

@Document(collection = "rawMaterials")
data class RawMaterial(
    val id: UUID,
    val type: String,
    val nutritionalGroup: String,
    val kcalPer100Gr: Double,
    val unit: String,
    val translations: List<Translation>
)

data class Translation(
    val language: String,
    val name: String,
    val description: String
)
