package com.starwars.trivia

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class TriviaApplication

fun main(args: Array<String>) {
	runApplication<TriviaApplication>(*args)
}
