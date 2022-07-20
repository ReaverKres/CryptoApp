package com.example.domain.usecase

import com.example.data.db.CurrencyDto
import com.example.data.repository.ExchangeRepositoryImpl
import com.example.domain.di.IoDispatcher
import com.example.domain.model.dto.FavoriteCurrencyDomain
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class DbKeys(
    val name: String,
    val baseName: String,
)

data class DbKeysForSave(
    val name: String,
    val value: Double,
    val baseCode: String,
)

@ViewModelScoped
class DeleteRateFromDbUseCase @Inject constructor(
    private val exchangeRepository: ExchangeRepositoryImpl,
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher
) : UseCase<DbKeys, Boolean>(coroutineDispatcher) {
    override suspend fun execute(parameters: DbKeys): Boolean {
        val (name, baseName) = parameters
        exchangeRepository.deleteRateFromDb(name, baseName)
        return true
    }
}

@ViewModelScoped
class SaveRateInDbUseCase @Inject constructor(
    private val exchangeRepository: ExchangeRepositoryImpl,
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher
) : UseCase<DbKeysForSave, Boolean>(coroutineDispatcher) {
    override suspend fun execute(parameters: DbKeysForSave): Boolean {
        val (name, value, baseCode) = parameters
        exchangeRepository.saveRateInDb(CurrencyDto(name, value, baseCode))
        return true
    }
}

@ViewModelScoped
class GetAllSavedRateDbUseCase @Inject constructor(
    private val exchangeRepository: ExchangeRepositoryImpl,
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, List<FavoriteCurrencyDomain>>(coroutineDispatcher) {
    override suspend fun execute(parameters: Unit): Flow<List<FavoriteCurrencyDomain>> {
        return exchangeRepository.getSavedRatesFromDb().map { list ->
            list.map { item ->
                FavoriteCurrencyDomain(
                    name = item.name, value = item.value,
                    baseName = item.baseName, baseValue = item.baseValue,
                )
            }
        }
    }
}
