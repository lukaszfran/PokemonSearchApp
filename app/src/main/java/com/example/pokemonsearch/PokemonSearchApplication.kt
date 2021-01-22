package com.example.pokemonsearch

import android.app.Application
import com.example.pokemon.model.Configuration
import com.example.pokemon.storage.Storage
import com.squareup.picasso.Picasso
import okhttp3.logging.HttpLoggingInterceptor

class PokemonSearchApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Picasso.get()
        Storage.initializeStorage(this)
        Configuration.httpLoggingLevel = HttpLoggingInterceptor.Level.BASIC
    }
}