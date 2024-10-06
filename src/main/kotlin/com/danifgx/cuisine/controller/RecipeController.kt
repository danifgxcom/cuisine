package com.danifgx.cuisine.controller

import com.danifgx.cuisine.service.RecipeService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/recipes")
class RecipeController(private val recipeService: RecipeService) {

    @GetMapping
    fun listAllRecipes() = recipeService.getAllRecipes()

    @GetMapping("/full")
    fun listFullRecipes() = recipeService.getAllRecipesWithEnrichedIngredients()

}



