package com.starwars.trivia.apis

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import graphql.com.google.common.collect.Lists
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.CachePut
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class StarWarsApi(
    @Value("\${star.wars.trivia.hostname}")
    val hostname: String
) {
    val webClient = WebClient.create(hostname)
    val log: Logger = LoggerFactory.getLogger(StarWarsApi::class.java)

    suspend fun getFilmsAndVehiclesByName(name: String): StarWarsApiSearchResponse? {
        return webClient
            .get()
            .uri {
                it.path("/api/people/")
                    .queryParam("search", name)
                    .build()
            }
            .retrieve()
            .bodyToMono(StarWarsApiSearchResponse::class.java)
            .doOnError {
                log.error("Error while calling search api on $name", it)
                StarWarsApiSearchResponse(Lists.newArrayList())
            }
            .awaitSingleOrNull()
    }

    @CachePut("film")
    suspend fun getFilmUrls(urls: List<String>): List<String> {
        return urls.map {url ->
            webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(FilmResponse::class.java)
                .doOnSuccess {
                    log.info("Obtained film name from url $url: $it")
                }
                .doOnError {
                    log.error("Error while fetching url $url", it)
                }
                .awaitSingleOrNull()
        }.map {
            it?.title ?: ""
        }
    }

    @CachePut("vehicle")
    suspend fun getVehicleUrls(urls: List<String>): List<String> {
        return urls.map {url ->
            webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(VehicleResponse::class.java)
                .doOnSuccess {
                    log.info("Obtained vehicle name from url $url: $it")
                }
                .doOnError {
                    log.error("Error while fetching url $url", it)
                }
                .awaitSingleOrNull()
        }.map {
            it?.name ?: ""
        }
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class StarWarsApiSearchResponse(
    val results: List<CharacterInfoResponse>
) {
    override fun toString(): String {
        return "$results \n"
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class CharacterInfoResponse(
    val name: String,
    @JsonProperty("films")
    val filmUrls: List<String>,
    @JsonProperty("vehicles")
    val vehicleUrls: List<String>
) {
    override fun toString(): String {
        return "Name:$name \n Films:$filmUrls \n Vehicles:$vehicleUrls\n"
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class FilmResponse(
    val title: String
) {
    override fun toString(): String {
        return "Title: $title"
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class VehicleResponse(
    val name: String
) {
    override fun toString(): String {
        return "Vehicle Name: $name"
    }
}