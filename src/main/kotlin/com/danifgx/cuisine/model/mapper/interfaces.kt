package com.danifgx.cuisine.model.mapper

import com.danifgx.cuisine.model.RawMaterial
import com.danifgx.cuisine.model.RawMaterialDTO
import com.danifgx.cuisine.model.Translation
import com.danifgx.cuisine.model.TranslationDTO
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
