package com.example.pokemonsearch.ui.favourites

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemon.storage.datamodel.FavouritePokemon
import com.example.pokemonsearch.R
import com.squareup.picasso.Picasso

class FavouritePokemonListItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var view: View = itemView
    private var pokemon: FavouritePokemon? = null

    private var image: ImageView
    private var name: TextView
    private var description: TextView

    init {
        image = view.findViewById(R.id.imageView)
        name = view.findViewById(R.id.name)
        description = view.findViewById(R.id.description)
    }

    fun bind(pokemon: FavouritePokemon) {
        this.pokemon = pokemon

        pokemon.sprites.frontDefault?.run {
            Picasso.get().load(Uri.parse(this)).into(image)
        }
        name.text = pokemon.name
        description.text = pokemon.descriptionTranslated
    }

}
