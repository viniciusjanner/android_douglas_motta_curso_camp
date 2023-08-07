package com.example.core.data.repository

import com.example.core.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    //
    // Flow
    // A palavra suspend é utilizada para operações executadas uma vez em uma thred em background.
    // Por isso não utilizamos suspend nas demais funções deste fluxo que retornam Flow.
    //
    fun getAll(): Flow<List<Character>>

    suspend fun isFavorite(characterId: Int): Boolean

    suspend fun saveFavorite(character: Character)

    suspend fun deleteFavorite(character: Character)
}
