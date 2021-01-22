package com.example.pokemon.network.networkdatamodel.shakespeare

data class ShakespeareTranslation(
    val contents: Contents,
    val success: Success?,
    val error: Error?
)