package com.example.marvelapp.presentation.detail

import androidx.annotation.StringRes

// Sufixo VE = ViewEntity

data class DetailChildVE(
    val id: Int,
    val imageUrl: String,
)

data class DetailParentVE(
    @StringRes
    val categoryStringResId: Int,
    val detailChildList: List<DetailChildVE>,
)
