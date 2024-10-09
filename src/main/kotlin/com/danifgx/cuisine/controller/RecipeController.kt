package com.danifgx.cuisine.controller

import com.danifgx.cuisine.model.Ingredient
import com.danifgx.cuisine.model.Recipe
import com.danifgx.cuisine.service.RecipeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/recipes")
class RecipeController(private val recipeService: RecipeService) {

    @GetMapping
    fun listAllRecipes() = recipeService.getAllRecipes()

    @GetMapping("/{id}")
    fun getRecipe(@PathVariable id: UUID): ResponseEntity<Recipe> {
        val recipe = recipeService.getRecipeById(id)
        return ResponseEntity.ok(recipe)
    }

    @GetMapping("/full")
    fun listFullRecipes() = recipeService.getAllRecipesWithEnrichedIngredients()

    @PostMapping
    fun createRecipe(@RequestBody recipe: Recipe): ResponseEntity<Recipe> {
        require(recipe.name.isNotBlank()) { "Recipe name must not be blank" }
        val createdRecipe = recipeService.createRecipe(recipe)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRecipe)
    }

    @PutMapping("/{id}")
    fun updateRecipe(@PathVariable id: UUID, @RequestBody updatedRecipe: Recipe): ResponseEntity<Recipe> {
        require(updatedRecipe.name.isNotBlank()) { "Recipe name must not be blank" }
        val updated = recipeService.updateRecipe(id, updatedRecipe)
        return ResponseEntity.ok(updated)
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