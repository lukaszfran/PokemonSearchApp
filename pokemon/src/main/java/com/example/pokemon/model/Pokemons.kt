package com.example.pokemon.model

import com.example.pokemon.network.*
import com.example.pokemon.network.networkdatamodel.pokemon.Pokemon
import com.example.pokemon.network.networkdatamodel.pokemon.Sprites
import com.example.pokemon.network.networkdatamodel.shakespeare.ShakespeareTranslation
import com.example.pokemon.storage.Storage
import com.example.pokemon.storage.datamodel.FavouritePokemon
import com.example.pokemon.ui.PokemonViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.Response
import java.net.SocketTimeoutException
import java.util.*


class Pokemons(
        private var pokemonService: PokemonApiService = PokemonApiService.create(Configuration.pokemonApiEndpoint),
        private var shakespeareService: ShakespeareTranslationApiService = ShakespeareTranslationApiService.create(Configuration.shakespeareTranslationApiEndpoint)
) {

    /**
     * Sequential way of chaining requests and feeding one to the other.
     * Returns ApiResult<PokemonViewModel> given Pokemon name
     * @param[name] - Pokemon name
     * @return Details of the requested pokemon or ApiResult.Failure
     */
    suspend fun getPokemonDetails(name: String) : ApiResult<PokemonViewModel> {

        return ApiCallHelper.safeApiResult<PokemonViewModel> {
            val e = pokemonService.pokemon(name.toLowerCase(Locale.getDefault()))

            if (!e.isSuccessful || e.body() == null) {
                return@safeApiResult Response.error(e.errorBody()!!, e.raw())
            }
            val pokemon = e.body()!!
            val speciesName: String? = pokemon.species.name
            var flavourText: String? = null
            if (speciesName != null) {
                val species = pokemonService.species(speciesName)
                if (species.isSuccessful) {
                    flavourText = species.body()?.getEnglishFlavourText()
                }
            }
            var translatedFlavour: String? = flavourText
            if (flavourText != null) {
                val shakespeareResponse = shakespeareService.translate(Configuration.shakespeareTranslationApiKey, flavourText)
                if (shakespeareResponse.isSuccessful && shakespeareResponse.body()?.contents != null) {
                    translatedFlavour = shakespeareResponse.body()?.contents?.translated
                }
            }

            return@safeApiResult Response.success(PokemonViewModel(pokemon.id, pokemon.name, pokemon.sprites, flavourText, translatedFlavour))
        }
    }

    /**
     * Chaining requests using a flow.
     * Returns ApiResult<PokemonViewModel> given Pokemon name
     * @param[name] - Pokemon name
     * @return Details of the requested pokemon or ApiResult.Failure
     */
    suspend fun getPokemonFlow(name: String) : Flow<ApiResult<PokemonViewModel>> {

        return flow<Pokemon> {
            val e = pokemonService.pokemon(name.toLowerCase(Locale.getDefault()))
            if (!e.isSuccessful) {
                throw ApiResultException("Error", ApiResult.Failure<Nothing>(e.errorBody()?.string() ?: "Failed to get Pokemon", null))
            }
            emit(e.body()!!)
        }
        .map {
            val e = pokemonService.species(it.species.name)
            if (!e.isSuccessful) {
                throw ApiResultException("Error", ApiResult.Failure<Nothing>(e.errorBody()?.string() ?: "Failed to fetch species", null))
            }
            return@map Pair(it, e.body()!!)
        }
        .map {
            var pokemon = it.first
            var flavourText = it.second.getEnglishFlavourText()
            if (flavourText != null) {
                val t = shakespeareService.translate(Configuration.shakespeareTranslationApiKey, flavourText)
                if (t.isSuccessful) {
                    return@map ApiResult.Success(PokemonViewModel(pokemon.id, pokemon.name, pokemon.sprites, flavourText, t.body()?.contents?.translated)) as ApiResult<PokemonViewModel>
                } else {
                    throw ApiResultException("Lost in tranlsation", ApiResult.Failure<Error>(t.errorBody()?.string() ?: "", null))
                }
            }
            return@map ApiResult.Success(PokemonViewModel(pokemon.id, pokemon.name, pokemon.sprites, flavourText, "")) as ApiResult<PokemonViewModel>
        }
        .catch { exception ->
            when(exception) {
                is ApiResultException -> {
                    emit(exception.result)
                }
                is SocketTimeoutException -> {
                    emit(ApiResult.Failure<Nothing>("Error: timeout", exception))
                }
                else -> {
                    emit(ApiResult.Failure<Nothing>("Error. Do you have internet connection? ", exception))
                }
            }
        }
        .flowOn(Dispatchers.IO)
    }

    /**
     * Gets pokemon by name
     * @param[name] name of the pokemon
     * @return .Success with Pokemon or .Failure @see[ApiResult]
     */
    suspend fun getPokemonSpriteUrl(name: String): ApiResult<Sprites> {
        val result = ApiCallHelper.safeApiResult {
            pokemonService.pokemon(name.toLowerCase(Locale.getDefault()))
        }
        return when (result) {
            is ApiResult.Success -> ApiResult.Success(result.value.sprites)
            is ApiResult.Failure<*> -> result
        }
    }

    /**
     * Returns translated pokemon description
     * @param[name] pokemon name of which description should be translated
     * @return ApiResult flow with the translated string
     */
    suspend fun getTranslatedPokemonDescription(name: String) : Flow<ApiResult<String>> {
        return getPokemonFlow(name)
                .map {
                    return@map when (it) {
                        is ApiResult.Success -> ApiResult.Success(it.value.shakespearenDescription ?: "")
                        is ApiResult.Failure<*> -> it
                    }
                }
    }

    /**
     * Returns all favourites pokemons in alphabetical order
     * @return list of pokemons
     */
    suspend fun getFavourites(): List<FavouritePokemon> {
        return Storage.getInstance().getFavourites()
    }

    /**
     * Adds a pokemon to list of favourites. If pokemon with such id already exists
     * it will be replaced with the given one.
     * @param[pokemon] pokemon that will be added to the list
     */
    suspend fun addToFavourites(pokemon: PokemonViewModel) {
        val fp = FavouritePokemon(pokemon.id, pokemon.name, pokemon.description ?: "", pokemon.shakespearenDescription ?: "", pokemon.sprites)
        return Storage.getInstance().addFavouritePokemon(fp)
    }

    suspend fun removeFromFavourites(pokemon: FavouritePokemon) {
        Storage.getInstance().removeFavouritePokemon(pokemon)
    }

    /**
     * Translates given text to it's Shakespearen version
     * @param[text] - text to be translated
     * @return ApiResult with ShakespeareTranslation or ApiResult.Failure
     */
    suspend fun translate(text: String) : ApiResult<ShakespeareTranslation> =
            ApiCallHelper.safeApiResult("", com.example.pokemon.network.networkdatamodel.shakespeare.Error::class.java) {
                shakespeareService.translate(Configuration.shakespeareTranslationApiKey, text)
            }
}
