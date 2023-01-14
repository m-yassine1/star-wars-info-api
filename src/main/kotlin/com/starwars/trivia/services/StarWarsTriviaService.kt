package com.starwars.trivia.services

import com.starwars.trivia.apis.StarWarsApi
import com.starwars.trivia.models.StarWarsCharacterInfo
import graphql.com.google.common.collect.Lists
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

interface StarWarsTriviaService {
    suspend fun getFilmsAndVehiclesByName(name: String): List<StarWarsCharacterInfo>
}

@Service
class StarWarsTriviaServiceImpl(
    val starWarsApi: StarWarsApi
): StarWarsTriviaService {
    val log: Logger = LoggerFactory.getLogger(StarWarsTriviaServiceImpl::class.java)

    override suspend fun getFilmsAndVehiclesByName(name: String) : List<StarWarsCharacterInfo> {
        val charactersInfo = starWarsApi.getFilmsAndVehiclesByName(name)
        log.info("Obtained result from search query: $charactersInfo")
        if (charactersInfo != null) {
            return charactersInfo.results.map {
                StarWarsCharacterInfo(
                    it.name,
                    starWarsApi.getFilmUrls(it.filmUrls),
                    starWarsApi.getVehicleUrls(it.vehicleUrls)
                )
            }
        }

        return Lists.newArrayList()
    }
}