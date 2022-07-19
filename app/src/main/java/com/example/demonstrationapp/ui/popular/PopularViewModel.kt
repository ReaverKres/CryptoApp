package com.example.demonstrationapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demonstrationapp.base.BaseViewModel
import com.example.domain.model.ExchangeResponse
import com.example.domain.model.Output
import com.example.domain.usecase.GetRatesNetworkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class PopularViewModel @Inject constructor(
    private val getRatesNetworkUseCase: GetRatesNetworkUseCase
) : BaseViewModel() {

    private val _data = MutableLiveData<Output<ExchangeResponse>>()
    val data: LiveData<Output<ExchangeResponse>> = _data

    init {
        getExchangeData()
    }

    fun getExchangeData(baseCurrency: String = "USD") {
        viewModelScope.launch {
            _data.postValue(Output.Loading)
            getRatesNetworkUseCase.invoke(baseCurrency)
                .catch { e ->
                    _data.postValue(Output.error(Exception(e)))
                }
                .collectLatest {
                    _data.postValue(Output.success(it))
                }
        }
    }

//    fun saveCurrency(name: String, value: Double) {
//        if (_data.value is ExchangeEvent.Success) {
//            val baseCode = (_data.value as ExchangeEvent.Success).result.baseName
//            viewModelScope.launch(Dispatchers.IO) {
//                currencyDao.insert(CurrencyDto(name, value, baseCode))
//            }
//        }
//    }
//
//    fun sortList(it: Int) {
//        if (_data.value is ExchangeEvent.Success) {
//            val result = (_data.value as ExchangeEvent.Success).result
//            val sort = when (it) {
//                0, 1 -> result.currencies.sortedBy { it.name }
//                else -> result.currencies.sortedBy { it.value }.asReversed()
//            }
//            viewModelScope.launch {
//                _data.emit(ExchangeEvent.Success(Currencies(result.baseName, sort)))
//            }
//        }
//    }
}