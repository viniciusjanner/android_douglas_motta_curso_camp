package com.example.core.data.repository

import com.example.core.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface FavoritesLocalDataSource {

    //
    // Flow
    // A palavra suspend é utilizada para operações executadas uma vez em uma thread em background.
    // Por isso não utilizamos suspend nas demais funções deste fluxo que retornam Flow.
    //
    fun getAll(): Flow<List<Character>>

    suspend fun isFavorite(characterId: Int): Boolean

    suspend fun save(character: Character)

    suspend fun delete(character: Character)
}
