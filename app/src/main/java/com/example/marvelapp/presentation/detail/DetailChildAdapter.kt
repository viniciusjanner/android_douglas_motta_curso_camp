package com.example.marvelapp.presentation.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelapp.databinding.ItemChildDetailBinding
import com.example.marvelapp.framework.imageloader.ImageLoader

class DetailChildAdapter(
    private val detailChildList: List<DetailChildVE>,
    private val imageLoader: ImageLoader,
) : RecyclerView.Adapter<DetailChildAdapter.DetailChildViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DetailChildViewHolder.create(parent, imageLoader)

    override fun onBindViewHolder(holder: DetailChildViewHolder, position: Int) =
        holder.bind(detailChildList[position])

    override fun getItemCount(): Int = detailChildList.size

    class DetailChildViewHolder(
        itemBinding: ItemChildDetailBinding,
        private val imageLoader: ImageLoader,
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        private val imageCategory: ImageView = itemBinding.imageCategory

        fun bind(detailChildVE: DetailChildVE) {
            imageLoader.load(imageCategory, detailChildVE.imageUrl)
        }

        companion object {
            fun create(
                parent: ViewGroup,
                imageLoader: ImageLoader,
            ): DetailChildViewHolder {
                val inflater = LayoutInflater.from(parent.context)

                val itemBinding = ItemChildDetailBinding.inflate(inflater, parent, false)

                return DetailChildViewHolder(itemBinding, imageLoader)
            }
        }
    }
}
