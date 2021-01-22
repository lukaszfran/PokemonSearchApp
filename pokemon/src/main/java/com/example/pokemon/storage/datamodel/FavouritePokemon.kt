package com.example.pokemon.storage.datamodel

import androidx.room.*
import com.example.pokemon.network.networkdatamodel.pokemon.Sprites

@Entity(tableName = "FavouritePokemon")
data class FavouritePokemon(
    @PrimaryKey @ColumnInfo(name = "id") var id: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "descriptionTranslated") var descriptionTranslated: String,
    @ColumnInfo(name = "sprites ") val sprites: Sprites
)

