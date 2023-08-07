package com.example.marvelapp.framework.di

import com.example.core.data.repository.CharactersRemoteDataSource
import com.example.core.data.repository.CharactersRepository
import com.example.marvelapp.framework.remote.RetrofitCharactersDataSource
import com.example.marvelapp.framework.repository.CharactersRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
interface CharactersRepositoryModule {

    @Binds
    fun bindCharactersRepository(repository: CharactersRepositoryImpl): CharactersRepository

    @Binds
    fun bindRemoteDataSource(dataSource: RetrofitCharactersDataSource): CharactersRemoteDataSource
}
