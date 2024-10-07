package com.danifgx.cuisine.service

import com.danifgx.cuisine.log.logger
import com.danifgx.cuisine.model.Ingredient
import com.danifgx.cuisine.model.Recipe
import com.danifgx.cuisine.repository.RawMaterialRepository
import com.danifgx.cuisine.repository.RecipeRepository
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.Aggregation.*
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.UUID

@Service
class RecipeService(
    private val recipeRepository: RecipeRepository,
    private val rawMaterialRepository: RawMaterialRepository,
    private val rawMaterialService: RawMaterialService,
    private val mongoTemplate: MongoTemplate,
) {

    private val logger = logger()

    fun getAllRecipes() = recipeRepository.findAll()

    fun getAllRecipesWithEnrichedIngredients(): List<Map<String, Any>> {

        logger.info("getAllRecipesWithEnrichedIngredients")
        val unwindIngredients = unwind("ingredients")

        val lookupOperation = lookup(
            "rawMaterials",
            "ingredients.rawMaterialId",
            "_id",
            "rawMaterial"
        )

        val unwindRawMaterial = unwind("rawMaterial", true)

        val groupByRecipe = group("_id")
            .first("name").`as`("name")
            .first("description").`as`("description")
            .first("diners").`as`("diners")
            .first("totalKCals").`as`("totalKCals")
            .first("author").`as`("author")
            .first("url").`as`("url")
            .push(
                mapOf(
                    "amount" to "\$ingredients.amount",
                    "rawMaterial" to "\$rawMaterial"
                )
            ).`as`("ingredients")

        val aggregation = Aggregation.newAggregation(
            unwindIngredients,
            lookupOperation,
            unwindRawMaterial,
            groupByRecipe
        )

        val results = mongoTemplate.aggregate(aggregation, "recipes", Map::class.java)

        val safeList = results.mappedResults.filterIsInstance<Map<String, Any>>()

        logger.info("Found ${safeList.size} results")

        return safeList
    }

    fun getRecipeById(id: UUID): Recipe {
        return recipeRepository.findById(id).orElseThrow {
            throw RecipeNotFoundException("Recipe with ID $id not found")
        }
    }

    fun createRecipe(recipe: Recipe): Recipe {
        return recipeRepository.save(recipe)
    }

    fun updateRecipe(id: UUID, updatedRecipe: Recipe): Recipe {
        if (!recipeRepository.existsById(id)) {
            throw RecipeNotFoundException("Recipe with ID $id not found")
        }
        return recipeRepository.save(updatedRecipe)
    }

    fun deleteRecipe(id: UUID) {
        if (!recipeRepository.existsById(id)) {
            throw RecipeNotFoundException("Recipe with ID $id not found")
        }
        recipeRepository.deleteById(id)
    }

    fun calculateIngredients(recipeId: UUID, targetDiners: Int): List<Ingredient> {
        val recipe = getRecipeById(recipeId)
        return calculateIngredientsForServings(recipe, targetDiners)
    }

    fun calculateIngredientsForServings(recipe: Recipe, targetDiners: Int): List<Ingredient> {
        val factor = targetDiners.toDouble() / recipe.diners
        return recipe.ingredients.map { ingredient ->
            ingredient.copy(amount = ingredient.amount * factor)
        }
    }

    fun findRecipesByIngredients(ingredientNames: List<String>): List<Recipe> {
        val rawMaterialIds = rawMaterialService.findRawMaterialIdsByNames(ingredientNames)

        if (rawMaterialIds.isEmpty()) {
            logger.info("No matching raw materials found for the provided ingredient names: $ingredientNames")
            return emptyList()
        }

        val rawMaterialIdsSet = rawMaterialIds.toSet()

        logger.info("Raw Material IDs to match: $rawMaterialIdsSet")

        return recipeRepository.findAll().filter { recipe ->
            val recipeIngredientIds = recipe.ingredients.map { it.rawMaterialId }.toSet()

            val containsAny = rawMaterialIdsSet.any { it in recipeIngredientIds }

            containsAny
        }
    }
}


@ResponseStatus(HttpStatus.NOT_FOUND)
class RecipeNotFoundException(message: String) : RuntimeException(message)