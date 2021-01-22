package com.example.pokemon.network

import com.example.pokemon.network.networkdatamodel.pokemon.Pokemon
import com.example.pokemon.network.networkdatamodel.pokemonspecies.Species
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Interface for the APIs used on the pokemon service to be used with Retrofit
 */
interface PokemonApiService {

    companion object {
        /**
         * @param[baseUrl] base url to be used with the service
         * @return Retrofit2 PokemonApiService
         */
        fun create(baseUrl: String): PokemonApiService =
                Utils.create(baseUrl, PokemonApiService::class.java)
    }

    @GET("/api/v2/pokemon/{name}/")
    suspend fun pokemon(@Path("name") name: String) : Response<Pokemon>
    
    @GET("/api/v2/pokemon-species/{name}/")
    suspend fun species(@Path("name") name: String) : Response<Species>

}