package com.danifgx.cuisine.model

import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID


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

