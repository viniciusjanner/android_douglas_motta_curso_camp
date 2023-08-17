package com.example.marvelapp.presentation.characters.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelapp.databinding.ItemCharacterRefreshStateBinding

class CharactersRefreshStateViewHolder(
    itemBinding: ItemCharacterRefreshStateBinding,
    retry: () -> Unit,
) : RecyclerView.ViewHolder(itemBinding.root) {

    private val progressBarLoadingMore = itemBinding.progressLoadingMore

    private val textTryAgainMessage = itemBinding.textTryAgain.also {
        it.setOnClickListener {
            retry()
        }
    }

    fun bind(loadSate: LoadState) {
        progressBarLoadingMore.isVisible = loadSate is LoadState.Loading
        textTryAgainMessage.isVisible = loadSate is LoadState.Error
    }

    companion object {
        fun create(
            parent: ViewGroup,
            retry: () -> Unit,
        ): CharactersRefreshStateViewHolder {
            val inflater = LayoutInflater.from(parent.context)

            val itemBinding = ItemCharacterRefreshStateBinding.inflate(inflater, parent, false)

            return CharactersRefreshStateViewHolder(itemBinding, retry)
        }
    }
}
