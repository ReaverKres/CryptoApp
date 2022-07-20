package com.example.data.repository

import com.example.data.model.ExchangeResponse
import com.example.data.services.ExchangeRateApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeRepositoryImpl @Inject constructor(
    private val api: ExchangeRateApi
) {
    suspend fun getRatesByCurrency(currency: String): Flow<ExchangeResponse> =
        withContext(Dispatchers.IO) {
            api.getExchangeRatesByCurrency(currency)
        }
}