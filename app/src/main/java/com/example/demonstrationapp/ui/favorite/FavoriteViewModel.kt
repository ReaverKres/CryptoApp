package com.example.demonstrationapp.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.dto.FavoriteCurrencyDomain
import com.example.domain.usecase.DbKeys
import com.example.domain.usecase.DeleteRateFromDbUseCase
import com.example.domain.usecase.GetAllSavedRateDbUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getAllSavedRateDbUseCase: GetAllSavedRateDbUseCase,
    private val deleteRateFromDbUseCase: DeleteRateFromDbUseCase
): ViewModel() {

    private val _data = MutableStateFlow<ExchangeEvent>(ExchangeEvent.Empty)
    val data: StateFlow<ExchangeEvent> = _data

    init {
        viewModelScope.launch {
            _data.value = ExchangeEvent.Loading
            getAllSavedRateDbUseCase.invoke(Unit).collect {
                if (it.isEmpty()){
                    _data.emit(ExchangeEvent.Empty)
                } else {
                    _data.emit(ExchangeEvent.Success(it))
                }
            }
        }
    }

    fun deleteCurrency(name: String, baseName: String) {
        viewModelScope.launch {
            deleteRateFromDbUseCase.invoke(DbKeys(name, baseName)) {}
        }
    }
}

sealed class ExchangeEvent {
    class Success(val result: List<FavoriteCurrencyDomain>) : ExchangeEvent()
    object Loading : ExchangeEvent()
    object Empty : ExchangeEvent()
}
