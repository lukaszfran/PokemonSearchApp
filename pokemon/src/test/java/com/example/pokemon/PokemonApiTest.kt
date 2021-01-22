package com.example.pokemon

import com.example.pokemon.network.ApiResult
import com.example.pokemon.model.Pokemons
import com.example.pokemon.network.PokemonApiService
import com.example.pokemon.network.ShakespeareTranslationApiService
import com.example.pokemon.network.networkdatamodel.pokemon.Sprites
import com.example.pokemon.ui.PokemonViewModel
import com.example.pokemon.utils.ResponseReader
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class PokemonApiTest {
    lateinit var service: Pokemons
    lateinit var mockWebServer: MockWebServer

    @Before
    fun beforeClass() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        val serverBaseUrl = mockWebServer.url("").toString()
        service = Pokemons(PokemonApiService.create(serverBaseUrl), ShakespeareTranslationApiService.create(serverBaseUrl))
    }

    @After
    fun afterClass() {
        mockWebServer.shutdown()
    }

    @Test
    fun checkDetailedPokemon() {
        mockWebServer.enqueue(MockResponse().apply {
            setBody(ResponseReader.readStringFromFile("mock_http_pokemon_success.json"))
        })
        mockWebServer.enqueue(MockResponse().apply {
            setBody(ResponseReader.readStringFromFile("mock_http_pokemon_species_success.json"))
        })
        mockWebServer.enqueue(MockResponse().apply {
            setBody(ResponseReader.readStringFromFile("mock_http_shakespeare_success.json"))
        })

        var pokemon = runBlocking {
            service.getPokemonDetails("ditto")
        }

        assert(pokemon is ApiResult.Success) { "Pokemon should be a ApiResult.Success" }
        val p: PokemonViewModel = (pokemon as ApiResult.Success<PokemonViewModel>).value
        assertEquals("ditto", p.name)
        assertEquals(96, p.description?.length ?: 0)
        assertEquals(116, p.shakespearenDescription?.length ?: 0)
    }

    @Test
    fun checkDetailedPokemonFlow() {
        mockWebServer.enqueue(MockResponse().apply {
            setBody(ResponseReader.readStringFromFile("mock_http_pokemon_success.json"))
        })
        mockWebServer.enqueue(MockResponse().apply {
            setBody(ResponseReader.readStringFromFile("mock_http_pokemon_species_success.json"))
        })
        mockWebServer.enqueue(MockResponse().apply {
            setBody(ResponseReader.readStringFromFile("mock_http_shakespeare_success.json"))
        })

        var pokemon = runBlocking {
            service.getPokemonFlow("ditto").first()
        }

        assert(pokemon is ApiResult.Success) { "Pokemon should be a ApiResult.Success" }
        val p: PokemonViewModel = (pokemon as ApiResult.Success<PokemonViewModel>).value
        assertEquals("ditto", p.name)
        assertEquals(96, p.description?.length ?: 0)
        assertEquals(116, p.shakespearenDescription?.length ?: 0)
    }

    @Test
    fun checkPokemonSprite() {
        mockWebServer.enqueue(MockResponse().apply {
            setBody(ResponseReader.readStringFromFile("mock_http_pokemon_success.json"))
        })
        var species = runBlocking {
            service.getPokemonSpriteUrl("wormadam")
        }

        assert(species is ApiResult.Success) { "Pokemon should be a ApiResult.Success" }
        val a: Sprites = (species as ApiResult.Success<Sprites>).value
        assertEquals("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/132.png", a.frontDefault)
    }

    @Test
    fun checkGetTranslatedDescription() {
        mockWebServer.enqueue(MockResponse().apply {
            setBody(ResponseReader.readStringFromFile("mock_http_pokemon_success.json"))
        })
        mockWebServer.enqueue(MockResponse().apply {
            setBody(ResponseReader.readStringFromFile("mock_http_pokemon_species_success.json"))
        })
        mockWebServer.enqueue(MockResponse().apply {
            setBody(ResponseReader.readStringFromFile("mock_http_shakespeare_success.json"))
        })

        var pokemonDescriptionResult = runBlocking {
            service.getTranslatedPokemonDescription("ditto").first()
        }

        val pokemonDescription: String = (pokemonDescriptionResult as ApiResult.Success<String>).value
        assertEquals(116, pokemonDescription.length)
    }

}