package com.example.domain.repository

import com.example.domain.model.DataFetchError
import com.example.domain.model.ExchangeResponse
import com.example.domain.model.NetworkResult


interface ExchangeRepository {
    suspend fun getRatesByCurrency(currency: String): NetworkResult<ExchangeResponse, DataFetchError>
}