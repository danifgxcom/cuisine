package com.danifgx.cuisine.controller

import com.danifgx.cuisine.log.logger
import com.danifgx.cuisine.service.RawMaterialService
import com.danifgx.cuisine.service.RawMaterialWithTranslation
import lombok.extern.log4j.Log4j
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/materials")
@Log4j
class RawMaterialController(private val rawMaterialService: RawMaterialService) {

    private val logger = logger()

    @GetMapping("/with-english-description")
    fun listWithEnglishDescription(): List<RawMaterialWithTranslation> {
        logger.info("Calling materials in English Description")
        return rawMaterialService.getAllRawMaterialsWithEnglishDescription("en")
    }

    @GetMapping("/with-spanish-description")
    fun listWithSpanishDescription(): List<RawMaterialWithTranslation> {
        return rawMaterialService.getAllRawMaterialsWithEnglishDescription("es")
    }

}



