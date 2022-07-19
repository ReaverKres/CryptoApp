package com.example.domain.di

import com.example.domain.usecase.CharactersUseCase
import com.example.domain.usecase.CharactersUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {
    @Binds
    @Singleton
    internal abstract fun bindCharacterUseCase(useCaseImpl: CharactersUseCaseImpl): CharactersUseCase
}