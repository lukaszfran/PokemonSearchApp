package com.example.pokemon.ui

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.pokemon.R
import kotlin.properties.Delegates

class PokemonDetailsConstraintLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var image: ImageView
    var title: TextView
    var description: TextView

    private var pokemonCardBackgroundColor by Delegates.notNull<Int>()
    private var imageBackgroundColor by Delegates.notNull<Int>()

    init {
        inflate(context, R.layout.pokemon_details, this)
        image = findViewById(R.id.imageView)
        title = findViewById(R.id.nameTextView)
        description = findViewById(R.id.descriptionTextView)
        setupAttributes(attrs)
    }

    fun setCardBackgroundColor(color: Int) {
        pokemonCardBackgroundColor = color
        this.setBackgroundColor(color)
    }

    fun setPokemonImageBackgroundColor(color: Int) {
        imageBackgroundColor = color
        image.setBackgroundColor(color)
    }

    private fun setupAttributes(attrs: AttributeSet?) {
        // 6
        // Obtain a typed array of attributes
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.PokemonDetailsConstraintLayout,
            0, 0)

        pokemonCardBackgroundColor = typedArray.getColor(R.styleable.PokemonDetailsConstraintLayout_pokemonCardBackgroundColor, Color.TRANSPARENT)
        imageBackgroundColor = typedArray.getColor(R.styleable.PokemonDetailsConstraintLayout_imageBackgroundColor, Color.TRANSPARENT)

        this.setBackgroundColor(pokemonCardBackgroundColor)
        image.setBackgroundColor(imageBackgroundColor)
        typedArray.recycle()
    }

    /**
     * Will apply all texts automatically into the view.
     * The image will not be loaded. Please load image yourself using a library of your choice.
     * @param[pokemon] pokemon details to be used
     */
    fun apply(pokemon: PokemonViewModel?) {
        title.text = pokemon?.name
        description.text = pokemon?.shakespearenDescription
    }
}