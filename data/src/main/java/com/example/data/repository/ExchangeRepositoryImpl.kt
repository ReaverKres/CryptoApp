package com.example.data.repository

import com.example.data.remote.util.toResult
import com.example.data.services.ExchangeRateApi
import com.example.domain.model.DataFetchError
import com.example.domain.model.ExchangeResponse
import com.example.domain.model.NetworkResult
import com.example.domain.repository.ExchangeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExchangeRepositoryImpl @Inject constructor(
    private val api: ExchangeRateApi
): ExchangeRepository {
    override suspend fun getRatesByCurrency(currency: String): NetworkResult<ExchangeResponse, DataFetchError> =
        withContext(Dispatchers.IO) {
            api.getExchangeRatesByCurrency(currency).toResult()
        }


}