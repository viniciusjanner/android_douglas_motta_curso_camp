package com.example.marvelapp.framework.di

import com.example.core.usecase.base.AppCoroutinesDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
object CoroutinesModules {

    @Provides
    fun provideDispatchers() = AppCoroutinesDispatchers(
        Dispatchers.IO,
        Dispatchers.Default,
        Dispatchers.Main,
    )
}
