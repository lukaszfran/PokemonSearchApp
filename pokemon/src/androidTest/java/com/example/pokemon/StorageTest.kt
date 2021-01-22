package com.example.pokemon

import android.content.Context
import androidx.room.Room
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.pokemon.network.networkdatamodel.pokemon.Sprites
import com.example.pokemon.storage.AppDatabase
import com.example.pokemon.storage.datamodel.FavouritePokemon
import com.example.pokemon.storage.datamodel.FavouritePokemonDao
import kotlinx.coroutines.runBlocking
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class StorageTest {
    private lateinit var pokemonsDao: FavouritePokemonDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java).build()
        pokemonsDao = db.favouritePokemonDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() {
        runBlocking {
            val sprites = Sprites("a", "b", "c", "d",
                "e", "f", "g", "h")
            pokemonsDao.insert(FavouritePokemon(1, "Olo", "desc", "trans", sprites))
            val p = pokemonsDao.getAllSortedByName()
            assertEquals(1, p.count())
            assertEquals("Olo", p.last().name)
            assertEquals("desc", p.last().description)
            assertEquals("trans", p.last().descriptionTranslated)
            assertEquals("e", p.last().sprites.frontDefault)
        }
    }
}