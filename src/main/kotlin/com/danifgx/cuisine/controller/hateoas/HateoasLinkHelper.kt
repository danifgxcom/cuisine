package com.danifgx.cuisine.controller.hateoas

import com.danifgx.cuisine.controller.RawMaterialController
import com.danifgx.cuisine.controller.RecipeController
import com.danifgx.cuisine.model.RawMaterialDTO
import com.danifgx.cuisine.model.RecipeDTO
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder

object HateoasLinkHelper {

    fun addSelfLinkToRawMaterialDTO(dto: RawMaterialDTO): RawMaterialDTO {
        val selfLink = WebMvcLinkBuilder.linkTo(
            WebMvcLinkBuilder.methodOn(RawMaterialController::class.java).getMaterialById(dto.id)
        ).withSelfRel()
        dto.add(selfLink)
        return dto
    }

    fun addLinksToRawMaterialCollection(
        dtoList: List<RawMaterialDTO>,
        language: String? = null
    ): CollectionModel<RawMaterialDTO> {
        val collectionLink = if (language != null) {
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(RawMaterialController::class.java).listWithDescription(language)
            ).withSelfRel()
        } else {
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(RawMaterialController::class.java).listAllMaterials()
            ).withSelfRel()
        }

        dtoList.forEach { addSelfLinkToRawMaterialDTO(it) }
        return CollectionModel.of(dtoList, collectionLink)
    }

    fun addSelfLinkToRecipe(recipeDTO: RecipeDTO): RecipeDTO {
        val selfLink = WebMvcLinkBuilder.linkTo(
            WebMvcLinkBuilder.methodOn(RecipeController::class.java).getRecipe(recipeDTO.id)
        ).withSelfRel()
        recipeDTO.add(selfLink)
        return recipeDTO
    }

    fun addLinksToRecipeCollection(recipeDTOList: List<RecipeDTO>): CollectionModel<RecipeDTO> {
        val collectionLink = WebMvcLinkBuilder.linkTo(
            WebMvcLinkBuilder.methodOn(RecipeController::class.java).listAllRecipes()
        ).withSelfRel()

        recipeDTOList.forEach { addSelfLinkToRecipe(it) }
        return CollectionModel.of(recipeDTOList, collectionLink)
    }
}