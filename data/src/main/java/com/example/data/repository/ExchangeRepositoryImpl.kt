package com.example.data.repository

import com.example.data.model.ExchangeResponse
import com.example.data.services.ExchangeRateApi
import com.example.data.db.CurrencyDao
import com.example.data.db.CurrencyDto
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeRepositoryImpl @Inject constructor(
    private val api: ExchangeRateApi,
    private val currencyDao: CurrencyDao
) {
    suspend fun getRatesFromNetworkByCurrency(currency: String): Flow<ExchangeResponse> =
        coroutineScope {
            api.getExchangeRatesByCurrency(currency)
        }

    suspend fun getSavedRatesFromDb(): Flow<List<CurrencyDto>> =
        coroutineScope {
           currencyDao.getAll()
        }

    suspend fun saveRateInDb(currencyDto: CurrencyDto): Unit =
        coroutineScope {
            currencyDao.insert(currencyDto)
        }

    suspend fun deleteRateFromDb(name: String, baseName: String): Unit =
        coroutineScope {
            val currencyFromDb = currencyDao.getCurrencyByKeys(name, baseName)
            currencyDao.delete(currencyFromDb)
        }
}