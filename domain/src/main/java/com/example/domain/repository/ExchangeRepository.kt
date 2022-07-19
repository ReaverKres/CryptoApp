package com.example.domain.repository

import com.example.domain.model.ExchangeResponse
import kotlinx.coroutines.flow.Flow


interface ExchangeRepository {
    suspend fun getRatesByCurrency(currency: String): Flow<ExchangeResponse>
}