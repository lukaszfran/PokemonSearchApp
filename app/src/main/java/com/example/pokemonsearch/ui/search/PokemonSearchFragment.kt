package com.example.pokemonsearch.ui.search

import android.app.SearchManager
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.os.Bundle
import android.provider.BaseColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pokemon.model.PokemonNames
import com.example.pokemon.ui.PokemonDetailsConstraintLayout
import com.example.pokemonsearch.R
import com.example.pokemonsearch.utils.hideKeyboard
import com.squareup.picasso.Picasso
import android.widget.CursorAdapter as CursorAdapter

class PokemonSearchFragment : Fragment() {

    private lateinit var searchViewModel: PokemonSearchViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        searchViewModel =
                ViewModelProvider(this).get(PokemonSearchViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_search, container, false)


        val pokemonView: PokemonDetailsConstraintLayout = root.findViewById(R.id.custom_pokemon_details)
        pokemonView.visibility = View.GONE
        val progressBar: ProgressBar = root.findViewById(R.id.progressBar)
        progressBar.visibility = View.GONE
        val favButton: Button = root.findViewById(R.id.add_to_favourites)
        favButton.visibility = View.GONE

        searchViewModel.isLoading.observe(viewLifecycleOwner, {
            if (it) {
                progressBar.visibility = View.VISIBLE
                pokemonView.visibility = View.GONE
            } else {
                pokemonView.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
        })

        searchViewModel.currentPokemon.observe(viewLifecycleOwner, {
            pokemonView.apply(it)
            it?.let {
                favButton.visibility = View.VISIBLE
            } ?: run {
                favButton.visibility = View.GONE
            }
            it?.sprites?.frontDefault?.let { urlString ->
               val uri = Uri.parse(urlString)
                Picasso.get().load(uri).into(pokemonView.image)
            } ?: run {
                pokemonView.image.setImageDrawable(null)
            }
        })

        searchViewModel.errorMessage.observe(viewLifecycleOwner, {
            pokemonView.description.text = it
        })

        setupFavouriteButton(root)
        setupSearchView(root)

        return root
    }

    private fun setupFavouriteButton(root: View) {
        val favButton: Button = root.findViewById(R.id.add_to_favourites)

        favButton.setOnClickListener {
            searchViewModel.addToFavourites()
        }
    }

    private fun setupSearchView(root: View) {
        val searchText: SearchView = root.findViewById(R.id.searchView)

        val from = arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1)
        val to = intArrayOf(R.id.item_label)
        val cursorAdapter = SimpleCursorAdapter(context, R.layout.search_suggestion_item, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)

        searchText.suggestionsAdapter = cursorAdapter

        searchText.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    searchViewModel.loadDetailsFlow(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val cursor = MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))
                newText?.let {
                    PokemonNames.all.forEachIndexed { index, suggestion ->
                        if (suggestion.contains(newText, true))
                            cursor.addRow(arrayOf(index, suggestion))
                    }
                }

                cursorAdapter.changeCursor(cursor)
                return true
            }
        })

        searchText.setOnSuggestionListener(object: SearchView.OnSuggestionListener{
            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }

            override fun onSuggestionClick(position: Int): Boolean {
                hideKeyboard()
                val cursor = searchText.suggestionsAdapter.getItem(position) as Cursor
                val selection = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1))
                searchText.setQuery(selection, false)
                selection?.let {
                    searchViewModel.loadDetailsFlow(it)
                }
                searchText.clearFocus()
                return true
            }
        })
    }

}