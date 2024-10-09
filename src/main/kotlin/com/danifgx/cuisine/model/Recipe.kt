package com.danifgx.cuisine.model

import org.springframework.hateoas.RepresentationModel
import java.util.UUID

data class RecipeDTO(
    val id: UUID,
    val name: String,
    val description: String,
    val diners: Int,
    val totalKCals: Double,
    val author: String,
    val url: String,
    val ingredients: List<IngredientDTO>
) : RepresentationModel<RecipeDTO>()

data class IngredientDTO(
    val rawMaterialId: UUID,
    val amount: Double,
    val rawMaterial: Map<String, Any>? = null
)

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
)
