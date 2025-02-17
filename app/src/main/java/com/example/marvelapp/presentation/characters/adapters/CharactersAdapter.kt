package com.example.marvelapp.presentation.characters.adapters

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.core.domain.model.Character
import com.example.marvelapp.framework.imageloader.ImageLoader
import com.example.marvelapp.util.OnCharacterItemClick

class CharactersAdapter(
    private val imageLoader: ImageLoader,
    private val onItemClick: OnCharacterItemClick,
) : PagingDataAdapter<Character, CharactersViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CharactersViewHolder.create(parent, imageLoader, onItemClick)

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        getItem(position)?.let { character ->
            holder.bind(character)
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Character>() {

            override fun areItemsTheSame(oldItem: Character, newItem: Character) =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: Character, newItem: Character) =
                oldItem == newItem
        }
    }
}
