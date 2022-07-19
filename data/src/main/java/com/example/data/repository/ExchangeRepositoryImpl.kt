package com.example.data.repository

import com.example.data.services.ExchangeRateApi
import com.example.domain.model.ExchangeResponse
import com.example.domain.repository.ExchangeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExchangeRepositoryImpl @Inject constructor(
    private val api: ExchangeRateApi
): ExchangeRepository {
    override suspend fun getRatesByCurrency(currency: String): Flow<ExchangeResponse> =
        withContext(Dispatchers.IO) {
            api.getExchangeRatesByCurrency(currency)
        }


}