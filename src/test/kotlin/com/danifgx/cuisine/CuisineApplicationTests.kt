package com.danifgx.cuisine

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@Import(TestcontainersConfiguration::class)
@SpringBootTest
class CuisineApplicationTests {

	@Test
	fun contextLoads() {
	}

}
