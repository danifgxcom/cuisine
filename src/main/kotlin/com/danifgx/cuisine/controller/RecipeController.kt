package com.danifgx.cuisine.controller

import com.danifgx.cuisine.controller.hateoas.HateoasLinkHelper
import com.danifgx.cuisine.model.Ingredient
import com.danifgx.cuisine.model.Recipe
import com.danifgx.cuisine.model.RecipeDTO
import com.danifgx.cuisine.model.mapper.RecipeMapper
import com.danifgx.cuisine.service.RecipeService
import org.springframework.hateoas.CollectionModel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/recipes")
class RecipeController(
    private val recipeService: RecipeService,
    private val recipeMapper: RecipeMapper
) {

    @GetMapping
    fun listAllRecipes(): ResponseEntity<CollectionModel<RecipeDTO>?> {
        val recipes = recipeService.getAllRecipes()
        val recipesDto = recipes.map { recipeMapper.toDTO(it) }
        val collectionModel = HateoasLinkHelper.addLinksToRecipeCollection(recipesDto)

        return ResponseEntity.ok(collectionModel)
    }

    @GetMapping("/{id}")
    fun getRecipe(@PathVariable id: UUID): ResponseEntity<out Any?> {
        val recipe = recipeService.getRecipeById(id)
        val recipeDTO = recipeMapper.toDTO(recipe)

        return ResponseEntity.ok(HateoasLinkHelper.addSelfLinkToRecipe(recipeDTO))
    }

    @GetMapping("/full")
    fun listFullRecipes(): ResponseEntity<List<Map<String, Any>>?> {
        val recipes = recipeService.getAllRecipesWithEnrichedIngredients()
        //val recipeEntities = recipes.map { recipeMapper.toDTO(it) }
        //val recipesDTO = recipeEntities.map { recipeMapper.toDTO(it) }
        //val collectionModel = HateoasLinkHelper.addLinksToRecipeCollection(recipesDTO)

        return ResponseEntity.ok(recipes)
    }

    @PostMapping
    fun createRecipe(@RequestBody recipe: Recipe): ResponseEntity<RecipeDTO> {
        require(recipe.name.isNotBlank()) { "Recipe name must not be blank" }
        val createdRecipe = recipeService.createRecipe(recipe)
        val recipeDTO = recipeMapper.toDTO(createdRecipe)
        return ResponseEntity.status(HttpStatus.CREATED).body(HateoasLinkHelper.addSelfLinkToRecipe(recipeDTO))
    }

    @PutMapping("/{id}")
    fun updateRecipe(@PathVariable id: UUID, @RequestBody updatedRecipe: Recipe): ResponseEntity<RecipeDTO> {
        require(updatedRecipe.name.isNotBlank()) { "Recipe name must not be blank" }
        val updated = recipeService.updateRecipe(id, updatedRecipe)
        val updatedDTO = recipeMapper.toDTO(updated)
        return ResponseEntity.ok(HateoasLinkHelper.addSelfLinkToRecipe(updatedDTO))
    }


    @DeleteMapping("/{id}")
    fun deleteRecipe(@PathVariable id: UUID): ResponseEntity<Unit> {
        recipeService.deleteRecipe(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/{id}/calculate")
    fun calculateIngredients(
        @PathVariable id: UUID,
        @RequestParam targetDiners: Int
    ): ResponseEntity<List<Ingredient>> {
        require(targetDiners > 0) { "Target diners must be greater than 0" }
        val ingredients = recipeService.calculateIngredients(id, targetDiners)
        return ResponseEntity.ok(ingredients)
    }

    @GetMapping("/search")
    fun searchRecipesByIngredients(@RequestParam ingredients: String): ResponseEntity<List<Map<String, Any>>> {
        val ingredientNames = ingredients.split(",").map { it.trim() }
        val enrichedRecipes = recipeService.findRecipesWithEnrichedIngredients(ingredientNames)
        return ResponseEntity.ok(enrichedRecipes)
    }
}
