package com.example.pokemon.network.networkdatamodel.pokemon

import com.google.gson.annotations.SerializedName

data class Pokemon(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("sprites")
    val sprites: Sprites,
    @SerializedName("species")
    var species: PokemonSpecies
)