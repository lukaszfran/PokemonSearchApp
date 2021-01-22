package com.example.pokemon.network.networkdatamodel.pokemonspecies

import com.google.gson.annotations.SerializedName

data class FlavorTextEntry(
    @SerializedName("flavor_text")
    val flavorText: String,
    val language: Language
)