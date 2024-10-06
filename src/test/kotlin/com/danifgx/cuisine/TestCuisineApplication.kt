package com.danifgx.cuisine

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<CuisineApplication>().with(TestcontainersConfiguration::class).run(*args)
}
