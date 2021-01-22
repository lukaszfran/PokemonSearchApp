package com.example.pokemonsearch

import android.app.Application
import com.example.pokemon.model.Configuration
import com.example.pokemon.storage.Storage
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockWebServer

class PokemonSearchApplication : Application() {
    companion object {
        var mockWebServer = MockWebServer()
    }
    override fun onCreate() {
        super.onCreate()
        Picasso.get()
        Storage.initializeStorage(this)
        MainScope().launch {
            setupConfig()
        }

    }


    private suspend fun setupConfig() {
        withContext(Dispatchers.IO) {
            Configuration.httpLoggingLevel = HttpLoggingInterceptor.Level.HEADERS
            Configuration.pokemonApiEndpoint = mockWebServer?.url("pokemon/").toString()
            Configuration.shakespeareTranslationApiEndpoint =
                mockWebServer?.url("shakespeare/").toString()
        }
    }
}