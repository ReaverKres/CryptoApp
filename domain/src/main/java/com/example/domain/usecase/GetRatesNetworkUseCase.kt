package com.example.domain.usecase

import com.example.data.repository.ExchangeRepositoryImpl
import com.example.data.model.ExchangeResponse
import com.example.domain.di.IoDispatcher
import com.example.domain.model.ExchangeEntity
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ViewModelScoped
class GetRatesNetworkUseCase @Inject constructor(
    private val exchangeRepository: ExchangeRepositoryImpl,
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher
) : FlowUseCase<String, ExchangeEntity>(coroutineDispatcher) {
    override suspend fun execute(parameters: String): Flow<ExchangeEntity> =
        exchangeRepository.getRatesByCurrency(parameters).map {
            ExchangeEntity(
                baseCode = it.baseCode,
                conversionRates = it.conversionRates,
                documentation = it.documentation,
                result = it.result,
                termsOfUse = it.baseCode,
                timeLastUpdateUnix = it.timeLastUpdateUnix,
            )
        }
}