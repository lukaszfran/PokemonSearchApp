package com.example.pokemon.network.networkdatamodel.shakespeare

import com.google.gson.annotations.SerializedName

data class Error(
    @SerializedName("error")
    val errorDetails: ErrorDetails
)
