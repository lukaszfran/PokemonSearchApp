package com.example.pokemon.model

import okhttp3.logging.HttpLoggingInterceptor

/**
 * Configuration object that will allow you to fine tune PokemonSearch SDK
 *
 * @property[pokemonApiEndpoint] - you can override pokemon endpoint. This may be
 *                                 very helpful if you want to use mock responses
 * @property[shakespeareTranslationApiEndpoint] - you can override translation endpoint. This may be
 *                                          very helpful if you want to use mock responses
 * @property[shakespeareTranslationApiKey] - translation API is rate limited if you want to overcome this problem
 *                                           and you've purchased a subscription provide your api key
 * @property[httpLoggingLevel] - select a level of http call logs. By default its HttpLoggingInterceptor.Level.NONE
 */
object Configuration {
    var pokemonApiEndpoint: String = "https://pokeapi.co"
    var shakespeareTranslationApiEndpoint = "https://api.funtranslations.com"
    var shakespeareTranslationApiKey = "0j8hght1UEcjBskSqY_YlAeF"
    var httpLoggingLevel: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.NONE
}
