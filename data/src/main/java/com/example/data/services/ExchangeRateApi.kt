package com.example.data.services

import com.example.domain.model.ExchangeResponse
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeRateApi {

    @GET("latest/{currency}")
    suspend fun getExchangeRatesByCurrency(@Path("currency") currency: String): Flow<ExchangeResponse>
}

data class ApiError(
    val error: Error?,
    val success: Boolean?
)

data class Error(
    val code: Int?,
    val info: String?
)