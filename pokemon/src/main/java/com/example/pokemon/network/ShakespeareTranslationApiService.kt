package com.example.pokemon.network

import com.example.pokemon.network.networkdatamodel.shakespeare.ShakespeareTranslation
import retrofit2.Response
import retrofit2.http.*

/**
 * Interface for the APIs used on the translation service to be used with Retrofit
 */
interface ShakespeareTranslationApiService {
    companion object {
        /**
         * @param[baseUrl] base url to be used with the service
         * @return Retrofit2 ShakespeareTranslationApiService
         */
        fun create(baseUrl: String): ShakespeareTranslationApiService =
                Utils.create(baseUrl, ShakespeareTranslationApiService::class.java)
    }

    @FormUrlEncoded
    @POST("/translate/shakespeare.json")
    suspend fun translate(@Header("X-Funtranslations-Api-Secret") apiKey: String, @Field("text") text: String) : Response<ShakespeareTranslation>
}