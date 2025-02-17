package com.example.marvelapp.framework.repository

import com.example.core.data.repository.StorageLocalDataSource
import com.example.core.data.repository.StorageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(
    private val storageLocalDataSource: StorageLocalDataSource,
) : StorageRepository {

    override val sortingFlow: Flow<String>
        get() = storageLocalDataSource.sortingFlow

    override suspend fun saveSorting(sorting: String) {
        storageLocalDataSource.saveSorting(sorting)
    }
}
