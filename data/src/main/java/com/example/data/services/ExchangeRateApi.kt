package com.example.data.services

import com.example.data.model.ExchangeResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeRateApi {

    @GET("latest/{currency}")
    fun getExchangeRatesByCurrency(@Path("currency") currency: String): Flow<ExchangeResponse>
}

data class ApiError(
    val error: Error?,
    val success: Boolean?
)

data class Error(
    val code: Int?,
    val info: String?
)