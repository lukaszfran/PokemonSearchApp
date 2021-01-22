package com.example.pokemon.network.networkdatamodel.pokemonspecies

import com.google.gson.annotations.SerializedName

data class Species(
        @SerializedName("flavor_text_entries")
    val flavorTextEntries: List<FlavorTextEntry>,
) {
    /**
     * Will attempt to get english version of the flavour text.
     * If english version will not be found it will fallback to first
     * available one.
     */
    fun getEnglishFlavourText() : String? {
        for (entry in flavorTextEntries) {
            if (entry.language.name == "en") {
                return entry.flavorText
            }
        }
        return flavorTextEntries.firstOrNull()?.flavorText
    }
}