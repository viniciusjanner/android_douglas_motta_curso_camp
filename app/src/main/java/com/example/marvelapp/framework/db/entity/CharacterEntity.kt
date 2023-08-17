package com.example.marvelapp.framework.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.core.data.DbConstants

@Entity(tableName = DbConstants.CHARACTERS_TABLE_NAME)
data class CharacterEntity(
    //
    // A API da Marvel retorna os dados em ordem alfabetica.
    // O Room ordena automaticamente pelo id.
    // Precisamos do atributo autoId para não termos problema com a ordenação dos dados.
    // Com o autoId nao perdemos a ordem alfabetica dos dados retornados pela API.
    //
    @PrimaryKey(autoGenerate = true)
    val autoId: Int = 0,
    @ColumnInfo(name = DbConstants.CHARACTERS_COLUMN_INFO_ID)
    val id: Int,
    @ColumnInfo(name = DbConstants.CHARACTERS_COLUMN_INFO_NAME)
    val name: String,
    @ColumnInfo(name = DbConstants.CHARACTERS_COLUMN_INFO_IMAGE_URL)
    val imageUrl: String,
)
