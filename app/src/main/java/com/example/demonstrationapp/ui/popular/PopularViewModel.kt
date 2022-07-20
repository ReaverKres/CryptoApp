package com.example.demonstrationapp.ui.popular

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.demonstrationapp.base.BaseViewModel
import com.example.domain.model.dto.Currencies
import com.example.domain.usecase.DbKeysForSave
import com.example.domain.usecase.GetRatesNetworkUseCase
import com.example.domain.usecase.SaveRateInDbUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularViewModel @Inject constructor(
    private val getRatesNetworkUseCase: GetRatesNetworkUseCase,
    private val saveRateInDbUseCase: SaveRateInDbUseCase,
) : BaseViewModel() {

    private val _data = MutableLiveData<ExchangeEvent>()
    val data: LiveData<ExchangeEvent> = _data

    var selectedSort = 0

    init {
        getExchangeData()
    }

    sealed class ExchangeEvent {
        class Success(val result: Currencies) : ExchangeEvent()
        class Failure(val errorText: String) : ExchangeEvent()
        object Loading : ExchangeEvent()
        object Empty : ExchangeEvent()
    }

    fun getExchangeData(baseCurrency: String = "USD") {
        viewModelScope.launch {
            _data.postValue(ExchangeEvent.Loading)
            getRatesNetworkUseCase.invoke(baseCurrency)
                .catch { e ->
                    _data.postValue(ExchangeEvent.Failure(e.localizedMessage.orEmpty()))
                }
                .collectLatest { exchange ->
                    val successData = ExchangeEvent.Success(
                        Currencies(exchange.baseCode, exchange.getRatesForCurrency(baseCurrency))
                    )
                    _data.postValue(successData)
                }
        }
    }

    fun saveCurrency(name: String, value: Double) {
        if (_data.value is ExchangeEvent.Success) {
            val baseCode = (_data.value as ExchangeEvent.Success).result.baseName
            viewModelScope.launch {
                saveRateInDbUseCase.invoke(DbKeysForSave(name, value, baseCode)) {}
            }
        }
    }

    fun sortList(sortType: Int) {
        if (_data.value is ExchangeEvent.Success) {
            selectedSort = sortType
            val result = (_data.value as ExchangeEvent.Success).result
            val sort = when (sortType) {
                0, 1 -> result.currencies.sortedBy { it.name }
                else -> result.currencies.sortedBy { it.value }.asReversed()
            }
            _data.value = ExchangeEvent.Success(Currencies(result.baseName, sort))
        }
    }
}