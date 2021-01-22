package com.example.pokemonsearch.ui.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemonsearch.R

class FavouritesFragment : Fragment() {

    private lateinit var favouritesViewModel: FavouritesViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        favouritesViewModel =
                ViewModelProvider(this).get(FavouritesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_favourites, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        favouritesViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })

        val recyclerView: RecyclerView = root.findViewById(R.id.recycler_view)
        linearLayoutManager = LinearLayoutManager(this.requireContext())
        recyclerView.layoutManager = linearLayoutManager

        val adapter = FavouritesRecyclerViewAdapter(ArrayList(), favouritesViewModel)
        recyclerView.adapter = adapter

        favouritesViewModel.favs.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                textView.visibility = View.GONE
            } else {
                textView.visibility = View.VISIBLE
            }
            adapter.updateListData(it)
        })

        favouritesViewModel.load()
        return root
    }
}