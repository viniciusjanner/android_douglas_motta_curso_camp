package com.example.marvelapp.framework.di

import com.example.core.data.repository.FavoritesLocalDataSource
import com.example.core.data.repository.FavoritesRepository
import com.example.marvelapp.framework.local.RoomFavoritesDataSource
import com.example.marvelapp.framework.repository.FavoritesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
interface FavoritesRepositoryModule {

    @Binds
    fun bindFavoritesRepository(repository: FavoritesRepositoryImpl): FavoritesRepository

    @Binds
    fun bindLocalDataSource(dataSource: RoomFavoritesDataSource): FavoritesLocalDataSource
}
