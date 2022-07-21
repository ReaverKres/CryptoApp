package com.example.demonstrationapp.ui.popular

import androidx.lifecycle.viewModelScope
import com.example.demonstrationapp.base.BaseViewModel
import com.example.domain.model.dto.CurrenciesDto
import com.example.domain.model.dto.Currency
import com.example.domain.model.dto.FavoriteCurrencyDomain
import com.example.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularViewModel @Inject constructor(
    private val getRatesNetworkUseCase: GetRatesNetworkUseCase,
    private val saveRateInDbUseCase: SaveRateInDbUseCase,
    private val getAllSavedRateDbUseCase: GetAllSavedRateDbUseCase,
    private val deleteRateFromDbUseCase: DeleteRateFromDbUseCase
) : BaseViewModel() {

    private val _data = MutableStateFlow<ExchangeEvent>(ExchangeEvent.Empty)
    val data: StateFlow<ExchangeEvent> = _data
    private val dbCurrencies: MutableList<FavoriteCurrencyDomain> = mutableListOf()
    var baseName = "USD"

    var selectedSort = 0

    init {
        getExchangeData(baseName)
        getSavedData()
    }

    private fun getSavedData() {
        viewModelScope.launch {
           getAllSavedRateDbUseCase.invoke(Unit).collectLatest {
                dbCurrencies.clear()
                dbCurrencies.addAll(it)
            }
        }
    }

    sealed class ExchangeEvent {
        class Success(val currenciesDto: CurrenciesDto) : ExchangeEvent()
        class Failure(val errorText: String) : ExchangeEvent()
        object Loading : ExchangeEvent()
        object Empty : ExchangeEvent()
    }

    fun getExchangeData(baseCurrency: String = "USD") {
        baseName = baseCurrency
        viewModelScope.launch {
            _data.emit(ExchangeEvent.Loading)

            getRatesNetworkUseCase.invoke(baseCurrency)
                .catch { e ->
                    _data.emit(ExchangeEvent.Failure(e.localizedMessage.orEmpty()))
                }
                .collectLatest { exchange ->
                    val mappedCurrency = exchange.getRatesForCurrency(baseCurrency)
                    mappedCurrency.forEachIndexed { index, currency  ->
                        dbCurrencies.forEach { dbCurrencies ->
                            if(baseCurrency == dbCurrencies.baseName && currency.name == dbCurrencies.name) {
                                currency.isSaved = true
                            }
                        }
                    }

                    val successData = ExchangeEvent.Success(
                        CurrenciesDto(exchange.baseCode, mappedCurrency, exchange.conversionRates.keys)
                    )
                    _data.emit(successData)
                }
        }
    }

    private fun updateItem(
        currencies: MutableList<Currency>,
        currency: Currency,
        baseName: String
    ) {
        val selectedItemIndex = currencies.indexOf(currency)
        val newItem = currency.copy(isSaved = currency.isSaved.not())
        currencies.removeAt(selectedItemIndex)
        currencies.add(selectedItemIndex, newItem)

        _data.value = ExchangeEvent.Success(CurrenciesDto(
            baseName,
            currencies,
        ))
    }

    fun saveCurrency(currency: Currency) {
        if (_data.value is ExchangeEvent.Success) {
            val currencies = (_data.value as ExchangeEvent.Success).currenciesDto.currencies.toMutableList()
            val baseName = (_data.value as ExchangeEvent.Success).currenciesDto.baseName
            viewModelScope.launch {
                saveRateInDbUseCase.invoke(DbKeysForSave(currency.name, currency.value, baseName)) {}
                updateItem(currencies, currency, baseName)
            }
        }
    }

    fun deleteCurrency(currency: Currency) {
        viewModelScope.launch {
            val currencies = (_data.value as ExchangeEvent.Success).currenciesDto.currencies.toMutableList()
            val baseName = (_data.value as ExchangeEvent.Success).currenciesDto.baseName
            deleteRateFromDbUseCase.invoke(DbKeys(currency.name, baseName)) {}
            updateItem(currencies, currency, baseName)
        }
    }

    fun sortList(sortType: Int) {
        if (_data.value is ExchangeEvent.Success) {
            selectedSort = sortType
            val result = (_data.value as ExchangeEvent.Success).currenciesDto
            val sortedList = when (sortType) {
                0, 1 -> result.currencies.sortedBy { it.name }
                else -> result.currencies.sortedBy { it.value }.asReversed()
            }
            _data.value = ExchangeEvent.Success(CurrenciesDto(
                result.baseName,
                sortedList,
            ))
        }
    }
}