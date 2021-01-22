package com.example.pokemon.storage.datamodel

import androidx.room.*

@Dao
internal interface FavouritePokemonDao {
    @Query("SELECT * FROM FavouritePokemon")
    suspend fun getAll() : List<FavouritePokemon>

    @Query("SELECT * FROM FavouritePokemon ORDER BY name")
    suspend fun getAllSortedByName() : List<FavouritePokemon>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pokemon: FavouritePokemon)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg pokemon: FavouritePokemon)

    @Transaction
    @Delete
    suspend fun delete(pokemon: FavouritePokemon)
}