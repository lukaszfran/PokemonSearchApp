package com.example.pokemonsearch.ui.favourites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemon.storage.datamodel.FavouritePokemon
import com.example.pokemonsearch.R

class FavouritesRecyclerViewAdapter(
        private var listOfPokemon: List<FavouritePokemon>,
        private var favsViewModel: FavouritesViewModel
) : RecyclerView.Adapter<FavouritePokemonListItem>() {

    fun updateListData(listOfPokemon: List<FavouritePokemon>) {
        this.listOfPokemon = listOfPokemon
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritePokemonListItem {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.favs_list_item, parent, false)
        return FavouritePokemonListItem(inflatedView)
    }

    override fun onBindViewHolder(holder: FavouritePokemonListItem, position: Int) {
        holder.bind(listOfPokemon[position])
        holder.itemView.setOnLongClickListener {
            favsViewModel.remove(listOfPokemon[position])
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return listOfPokemon.size
    }

}

