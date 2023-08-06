package com.example.marvelapp.framework.di

import com.example.core.usecase.AddFavoriteUseCase
import com.example.core.usecase.AddFavoriteUseCaseImpl
import com.example.core.usecase.GetCharacterCategoriesUseCase
import com.example.core.usecase.GetCharacterCategoriesUseCaseImpl
import com.example.core.usecase.GetCharactersUseCase
import com.example.core.usecase.GetCharactersUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Suppress("unused")
@Module
@InstallIn(ViewModelComponent::class)
interface UseCaseModule {

    @Binds
    fun bindGetCharactersUseCase(useCase: GetCharactersUseCaseImpl): GetCharactersUseCase

    @Binds
    fun bindGetCharacterCategoriesUseCase(useCase: GetCharacterCategoriesUseCaseImpl): GetCharacterCategoriesUseCase

    @Binds
    fun bindAddFavoriteUseCase(useCase: AddFavoriteUseCaseImpl): AddFavoriteUseCase
}
