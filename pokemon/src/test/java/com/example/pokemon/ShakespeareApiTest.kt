package com.example.pokemon

import com.example.pokemon.model.Pokemons
import com.example.pokemon.network.ApiResult
import com.example.pokemon.network.PokemonApiService
import com.example.pokemon.network.ShakespeareTranslationApiService
import com.example.pokemon.network.networkdatamodel.shakespeare.Error
import com.example.pokemon.network.networkdatamodel.shakespeare.ShakespeareTranslation
import com.example.pokemon.utils.ResponseReader
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ShakespeareApiTest {
    lateinit var service: Pokemons
    lateinit var mockWebServer: MockWebServer

    @Before
    fun beforeClass() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        service = Pokemons(
                PokemonApiService.create(mockWebServer.url("").toString()),
                ShakespeareTranslationApiService.create(mockWebServer.url("").toString()))
    }

    @After
    fun afterClass() {
        mockWebServer.shutdown()
    }

    @Test
    fun checkTranslation() {
        mockWebServer.enqueue(MockResponse().apply {
            setBody(ResponseReader.readStringFromFile("mock_http_shakespeare_success.json"))
        })
        var translation = runBlocking {
            service.translate("myText")
        }

        assert(translation is ApiResult.Success)
        val a: ShakespeareTranslation = (translation as ApiResult.Success<ShakespeareTranslation>).value
        Assert.assertEquals(1, a.success?.total ?: 0)
        Assert.assertEquals("shakespeare", a.contents.translation)
        Assert.assertEquals("Thee did giveth mr. Tim a hearty meal,  but unfortunately what he did doth englut did maketh him kicketh the bucket.", a.contents.translated)
        Assert.assertNull(a.error)
    }

    @Test
    fun checkTranslationFailure() {
        mockWebServer.enqueue(MockResponse().apply {
            setResponseCode(400)
            setBody(ResponseReader.readStringFromFile("mock_http_shakespeare_failure.json"))
        })
        var translation = runBlocking {
            service.translate("myText")
        }

        assert(translation is ApiResult.Failure<*>) {"Returned type should be ApiResult.Failure"}
        val a  = (translation as ApiResult.Failure<Error>)
        Assert.assertNotNull(a.errorResponse?.errorDetails?.message)
        Assert.assertEquals("Bad Request: text is missing.",a.errorResponse?.errorDetails?.message)
    }
}
