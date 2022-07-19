package com.example.data.remote

import com.example.data.BaseRemoteDataSource
import com.example.data.services.ApiService
import apps.hm.mhchars.domain.model.CharacterEntity
import com.example.domain.model.Output
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * RemoteDataSource of Characters API service
 * @param apiService the object of api service
 */
class CharsRemoteDataSource @Inject constructor(
    private val apiService: ApiService, retrofit: Retrofit
) : BaseRemoteDataSource(retrofit) {

    /**
     * Method to fetch the characters from CharsRemoteDataSource
     * @return Outputs with list of Characters
     */
    suspend fun fetchCharacters(): Output<List<CharacterEntity>> {
        return getResponse(
            request = { apiService.getCharacters() }
        )
    }
}