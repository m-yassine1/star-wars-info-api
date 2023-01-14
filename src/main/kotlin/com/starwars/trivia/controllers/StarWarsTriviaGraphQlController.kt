package com.starwars.trivia.controllers

import com.starwars.trivia.models.StarWarsCharacterInfo
import com.starwars.trivia.services.StarWarsTriviaService
import graphql.com.google.common.collect.Lists
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class StarWarsTriviaGraphQlController(
    val starWarsTriviaService: StarWarsTriviaService
) {
    val log: Logger = LoggerFactory.getLogger(StarWarsTriviaGraphQlController::class.java)

    @QueryMapping
    suspend fun filmsAndVehiclesByName(@Argument name: String): List<StarWarsCharacterInfo> {
        log.info("Querying for $name")
        return starWarsTriviaService.getFilmsAndVehiclesByName(name)
    }
}