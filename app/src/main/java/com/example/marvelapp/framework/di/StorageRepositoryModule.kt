package com.example.marvelapp.framework.di

import com.example.core.data.repository.StorageLocalDataSource
import com.example.core.data.repository.StorageRepository
import com.example.marvelapp.framework.local.DataStorePreferencesDataSource
import com.example.marvelapp.framework.repository.StorageRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
interface StorageRepositoryModule {

    @Binds
    fun bindStorageRepository(repository: StorageRepositoryImpl): StorageRepository

    @Singleton
    @Binds
    fun bindLocalDataSource(dataSource: DataStorePreferencesDataSource): StorageLocalDataSource
}
