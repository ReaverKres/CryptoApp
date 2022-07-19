package com.example.domain.usecase

import com.example.domain.di.IoDispatcher
import com.example.domain.model.DataFetchError
import com.example.domain.model.ExchangeResponse
import com.example.domain.model.NetworkResult
import com.example.domain.repository.ExchangeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRatesNetworkUseCase @Inject constructor(
    private val exchangeRepository: ExchangeRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
    ) : FlowUseCase<String, ExchangeResponse>(dispatcher) {
    override suspend fun execute(parameters: String): Flow<ExchangeResponse> =
        exchangeRepository.getRatesByCurrency(parameters)
}