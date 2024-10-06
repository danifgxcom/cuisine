package com.danifgx.cuisine.init

import com.danifgx.cuisine.model.RawMaterial
import com.danifgx.cuisine.model.Recipe
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate
import java.nio.file.Files
import java.nio.file.Paths

@Configuration
class MongoDataLoader(private val mongoTemplate: MongoTemplate) {

    @Bean
    fun loadData(): CommandLineRunner {
        return CommandLineRunner {
            val objectMapper = jacksonObjectMapper()

            // Load RawMaterials
            val rawMaterialsPath = Paths.get("src/main/resources/rawMaterials.json")
            val rawMaterialsData = Files.readString(rawMaterialsPath)
            val rawMaterials: List<RawMaterial> = objectMapper.readValue(rawMaterialsData)
            rawMaterials.forEach { mongoTemplate.save(it, "rawMaterials") }

            // Load Recipes
            val recipesPath = Paths.get("src/main/resources/recipes.json")
            val recipesData = Files.readString(recipesPath)
            val recipes: List<Recipe> = objectMapper.readValue(recipesData)
            recipes.forEach { mongoTemplate.save(it, "recipes") }

        }
    }
}
