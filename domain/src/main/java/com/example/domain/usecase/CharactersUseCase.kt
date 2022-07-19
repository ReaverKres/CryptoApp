package com.example.domain.usecase

import apps.hm.mhchars.domain.model.CharacterEntity
import com.example.domain.model.Output
import kotlinx.coroutines.flow.Flow

/**
 * Interface of Characters UseCase to expose to ui module
 */
interface CharactersUseCase {
    /**
     * UseCase Method to fetch the characters from Data Layer
     */
    suspend fun execute(): Flow<Output<List<CharacterEntity>>>
}