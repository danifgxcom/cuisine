package com.danifgx.cuisine.repository

import com.danifgx.cuisine.model.RawMaterial
import com.danifgx.cuisine.model.Recipe
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface RawMaterialRepository : MongoRepository<RawMaterial, UUID> {
    @Query("{ 'translations.name': { \$in: ?0 } }")
    fun findByTranslationNames(names: List<String>): List<RawMaterial>
}


@Repository
interface RecipeRepository : MongoRepository<Recipe, UUID>