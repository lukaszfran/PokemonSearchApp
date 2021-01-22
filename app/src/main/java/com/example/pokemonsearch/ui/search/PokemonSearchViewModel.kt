package com.example.pokemonsearch.ui.search

import androidx.lifecycle.*
import com.example.pokemon.model.Pokemons
import com.example.pokemon.network.ApiResult
import com.example.pokemon.ui.PokemonViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PokemonSearchViewModel() : ViewModel() {

    private val pokemons: Pokemons = Pokemons()
    private var _currentPokemon = MutableLiveData<PokemonViewModel?>()
    var currentPokemon: LiveData<PokemonViewModel?> = _currentPokemon

    private var _errorMessage = MutableLiveData<String?>()
    var errorMessage: LiveData<String?> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    /**
     * Load details from flow
     */
    fun loadDetailsFlow(name: String) {
        _isLoading.apply { value = true }
        viewModelScope.launch {
            pokemons.getPokemonFlow(name).collect {
                applyResults(it)
                _isLoading.apply { value = false }
            }
        }
    }

    /**
     * Load details from standard suspended function
     */
    fun loadDetails(name: String) {
        _isLoading.apply { value = true }
        viewModelScope.launch {
            val result = pokemons.getPokemonDetails(name)
            applyResults(result)
            _isLoading.apply { value = false }
        }
    }

    private fun applyResults(result: ApiResult<PokemonViewModel>) {
        if (result is ApiResult.Success) {
            _errorMessage.apply { value = null }
            _currentPokemon.apply {
                value = result.value
            }
        } else if (result is ApiResult.Failure<*>){
            _currentPokemon.apply { value = null }
            _errorMessage.apply { value = result.message }
        }
    }

    fun addToFavourites() {
        currentPokemon.value?.let {
            viewModelScope.launch {
                pokemons.addToFavourites(it)
            }
        }

    }
}