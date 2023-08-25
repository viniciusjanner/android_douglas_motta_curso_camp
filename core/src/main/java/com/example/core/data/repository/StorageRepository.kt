package com.example.core.data.repository

import kotlinx.coroutines.flow.Flow

interface StorageRepository {

    val sortingFlow: Flow<String>

    suspend fun saveSorting(sorting: String)
}
