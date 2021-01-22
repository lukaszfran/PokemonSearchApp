package com.example.pokemon.storage.datamodel

import androidx.room.TypeConverter
import com.example.pokemon.network.networkdatamodel.pokemon.Sprites
import com.google.gson.Gson

internal class Converters {

    @TypeConverter
    fun toJson(value: Sprites?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToSprites(value: String): Sprites = Gson().fromJson(value, Sprites::class.java)
}