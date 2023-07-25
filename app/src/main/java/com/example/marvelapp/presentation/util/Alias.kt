package com.example.marvelapp.presentation.util

import android.view.View
import com.example.core.domain.model.Character

typealias OnCharacterItemClick = (character: Character, view: View) -> Unit
