package com.danifgx.cuisine.service

import com.danifgx.cuisine.repository.RecipeRepository
import lombok.extern.log4j.Log4j2
import org.slf4j.LoggerFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.Aggregation.group
import org.springframework.data.mongodb.core.aggregation.Aggregation.lookup
import org.springframework.data.mongodb.core.aggregation.Aggregation.unwind
import org.springframework.stereotype.Service

@Service
class RecipeService(
    private val repository: RecipeRepository,
    private val mongoTemplate: MongoTemplate
) {
    private val logger = LoggerFactory.getLogger(RecipeService::class.java)

    fun getAllRecipes() = repository.findAll()

    fun getAllRecipesWithEnrichedIngredients(): List<Map<String, Any>> {
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



        return results.mappedResults as List<Map<String, Any>>
    }
}
