package com.danifgx.cuisine.model.mapper

import com.danifgx.cuisine.model.*
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

@Mapper(componentModel = "spring")
interface RawMaterialMapper {

    companion object {
        val INSTANCE: RawMaterialMapper = Mappers.getMapper(RawMaterialMapper::class.java)
    }

    fun toDTO(rawMaterial: RawMaterial): RawMaterialDTO

    fun toEntity(rawMaterialDTO: RawMaterialDTO): RawMaterial
}

@Mapper(componentModel = "spring")
interface TranslationMapper {

    companion object {
        val INSTANCE: TranslationMapper = Mappers.getMapper(TranslationMapper::class.java)
    }

    fun toDTO(translation: Translation): TranslationDTO

    fun toEntity(translationDTO: TranslationDTO): Translation
}

@Mapper(componentModel = "spring")
interface RecipeMapper {

    companion object {
        val INSTANCE: RecipeMapper = Mappers.getMapper(RecipeMapper::class.java)
    }

    fun toDTO(recipe: Recipe): RecipeDTO

    fun toEntity(recipeDTO: RecipeDTO): Recipe
}

@Mapper(componentModel = "spring")
interface IngredientMapper {

    companion object {
        val INSTANCE: IngredientMapper = Mappers.getMapper(IngredientMapper::class.java)
    }

    fun toDTO(ingredient: Ingredient): IngredientDTO

    fun toEntity(ingredientDTO: IngredientDTO): Ingredient
}