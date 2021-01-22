package com.example.pokemon.network

import com.example.pokemon.model.Configuration
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Utils {
    /**
     * @param[baseUrl] base url to be used with the service
     * @return Retrofit2 PokemonApiService
     */
    fun <T> create(baseUrl: String, apiService: Class<T>): T {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(Configuration.httpLoggingLevel)

        // OkHttpClient setup
        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(7, TimeUnit.SECONDS)
                .addNetworkInterceptor(logging)
                .build()

        val retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(
                        GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build()
        return retrofit.create(apiService)
    }
}