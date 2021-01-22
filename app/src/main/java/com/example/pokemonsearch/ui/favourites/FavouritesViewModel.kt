package com.example.pokemonsearch.ui.favourites

import androidx.lifecycle.*
import com.example.pokemon.model.Pokemons
import com.example.pokemon.storage.datamodel.FavouritePokemon
import kotlinx.coroutines.launch

class FavouritesViewModel : ViewModel() {

    private val pokemons: Pokemons = Pokemons()

    private val _text = MutableLiveData<String>().apply {
        value = "This is Favourites Fragment"
    }
    val text: LiveData<String> = _text

    private val _favs = MutableLiveData<List<FavouritePokemon>>()
    val favs: LiveData<List<FavouritePokemon>> = _favs


    fun load() {
        viewModelScope.launch {
            val newFavs = pokemons.getFavourites()
            _favs.apply {
                value = newFavs
            }
        }
    }

    fun remove(pokemon: FavouritePokemon) {
        viewModelScope.launch {
            pokemons.removeFromFavourites(pokemon)
            load()
        }
    }
}