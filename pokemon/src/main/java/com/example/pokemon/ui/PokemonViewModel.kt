package com.example.pokemon.ui

import com.example.pokemon.network.networkdatamodel.pokemon.Sprites

/**
 * Combined pokemon detail information
 * @property[id] pokemon id
 * @property[name] pokemon name
 * @property[sprites] all available images of pokemon
 * @property[description] description if available or null
 * @property[shakespearenDescription] description in Shakespeare's language or null
 */
data class PokemonViewModel(
    var id: Int,
    var name: String,
    var sprites: Sprites,
    var description: String?,
    var shakespearenDescription: String?
)
