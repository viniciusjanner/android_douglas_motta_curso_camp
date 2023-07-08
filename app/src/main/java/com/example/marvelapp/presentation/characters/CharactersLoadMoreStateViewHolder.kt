package com.example.marvelapp.presentation.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelapp.databinding.ItemCharacterLoadMoreStateBinding

class CharactersLoadMoreStateViewHolder(
    itemBinding: ItemCharacterLoadMoreStateBinding,
    retry: () -> Unit,
) : RecyclerView.ViewHolder(itemBinding.root) {

    private val progressLoadingMore = itemBinding.progressLoadingMore
    private val textTryAgain = itemBinding.textTryAgain.also {
        it.setOnClickListener {
            retry()
        }
    }

    fun bind(loadState: LoadState) {
        progressLoadingMore.isVisible = loadState is LoadState.Loading
        textTryAgain.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): CharactersLoadMoreStateViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemBinding = ItemCharacterLoadMoreStateBinding
                .inflate(inflater, parent, false)
            return CharactersLoadMoreStateViewHolder(itemBinding, retry)
        }
    }
}
