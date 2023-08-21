package com.example.marvelapp.presentation.characters.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.core.domain.model.Character
import com.example.marvelapp.databinding.ItemCharactersBinding
import com.example.marvelapp.framework.imageloader.ImageLoader
import com.example.marvelapp.util.OnCharacterItemClick

class CharactersViewHolder(
    itemBinding: ItemCharactersBinding,
    private val imageLoader: ImageLoader,
    private val onItemClick: OnCharacterItemClick,
) : RecyclerView.ViewHolder(itemBinding.root) {

    private val textName: TextView = itemBinding.textName
    private val imageCharacter: ImageView = itemBinding.imageCharacter

    fun bind(character: Character) {
        textName.text = character.name

        imageCharacter.transitionName = character.name

        imageLoader.load(imageCharacter, character.imageUrl)

        itemView.setOnClickListener {
            onItemClick.invoke(character, imageCharacter)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            imageLoader: ImageLoader,
            onItemClick: OnCharacterItemClick,
        ): CharactersViewHolder {
            val inflater = LayoutInflater.from(parent.context)

            val itemBinding = ItemCharactersBinding.inflate(inflater, parent, false)

            return CharactersViewHolder(itemBinding, imageLoader, onItemClick)
        }
    }
}
