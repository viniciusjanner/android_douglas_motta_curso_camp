package com.example.marvelapp.framework.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.data.DbConstants
import com.example.marvelapp.framework.db.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Suppress("unused")
@Dao
interface FavoriteDao {

    //
    // Flow
    // A palavra suspend é utilizada para operações executadas uma vez em uma thred em background.
    // Por isso não utilizamos suspend nas demais funções deste fluxo que retornam Flow.
    //
    @Query("SELECT * FROM ${DbConstants.FAVORITES_TABLE_NAME}")
    fun loadFavorites(): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM ${DbConstants.FAVORITES_TABLE_NAME} WHERE id = :characterId")
    suspend fun hasFavorite(characterId: Int): FavoriteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favoriteEntity: FavoriteEntity)

    @Delete
    suspend fun deleteFavorite(favoriteEntity: FavoriteEntity)
}
