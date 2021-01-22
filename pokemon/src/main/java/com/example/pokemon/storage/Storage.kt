package com.example.pokemon.storage

import android.content.Context
import androidx.room.Room
import com.example.pokemon.storage.datamodel.FavouritePokemon
import kotlin.IllegalStateException

/**
 * Room implementation for storing favourites pokemons
 */
class Storage (applicationContext: Context) {
    private var db : AppDatabase = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java, "PokemonSearchStore"
    ).build()

    companion object {
        private var instance: Storage? = null

        /**
         * Returns instance of Storage or throws.
         * Before this function can be used make sure that @see[initializeStorage] was called at least once.
         * @return Storage instance
         */
        @Throws(IllegalStateException::class)
        fun getInstance() : Storage {
            if (instance == null) {
                throw IllegalStateException("Storage is not initialized. Please call Storage.initializeStorage(context) before use of Storage instance")
            }
            return instance!!
        }

        /**
         * Initialization of the Storage singleton.
         * This function needs to be called in onCreate of the android.app.Application
         * @param[context] - application context
         */
        fun initializeStorage(context: Context) {
            if (instance == null) {
                instance = Storage(context)
                instance!!.db
            }
        }
    }

    /**
     * Returns all favourites pokemons in alphabetical order
     * @return list of pokemons
     */
    internal suspend fun getFavourites() : List<FavouritePokemon> {
        return db.favouritePokemonDao().getAllSortedByName()
    }

    /**
     * Adds a pokemon to list of favourites. If pokemon with such id already exists
     * it will be replaced with the given one.
     * @param[pokemon] pokemon that will be added to the list
     */
    internal suspend fun addFavouritePokemon(pokemon: FavouritePokemon) {
        db.favouritePokemonDao().insert(pokemon)
    }

    /**
     * Removes a pokemon from list of favourites. If pokemon with such id does't exist
     * it will be ignored.
     * @param[pokemon] pokemon to be removed
     */
    internal suspend fun removeFavouritePokemon(pokemon: FavouritePokemon) {
        db.favouritePokemonDao().delete(pokemon)
    }

}