package com.danifgx.cuisine.model

import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID

@Document(collection = "recipes")
data class Recipe(
    val id: UUID,
    val name: String,
    val description: String,
    val ingredients: List<Ingredient>,
    val diners: Int,
    val totalKCals: Double,
    val author: String,
    val url: String
)

data class Ingredient(
    val rawMaterialId: UUID,
    val amount: Double,
    val unit: String?,
)