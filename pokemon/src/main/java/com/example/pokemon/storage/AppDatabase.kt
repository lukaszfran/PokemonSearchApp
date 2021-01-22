package com.example.pokemon.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pokemon.storage.datamodel.Converters
import com.example.pokemon.storage.datamodel.FavouritePokemon
import com.example.pokemon.storage.datamodel.FavouritePokemonDao

@Database(entities = [FavouritePokemon::class], version = 2)
@TypeConverters(Converters::class)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun favouritePokemonDao() : FavouritePokemonDao
}