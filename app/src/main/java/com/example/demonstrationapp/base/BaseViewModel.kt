/*
 * Copyright 2018 LWO LLC
 */

package com.example.demonstrationapp.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demonstrationapp.utils.Event
import com.example.demonstrationapp.utils.LoadingCounter
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    protected val loadingState = LoadingCounter()
    val isLoading = loadingState.observable

    private val _error = MutableLiveData<Event<Exception>>()
    val error: LiveData<Event<Exception>>
        get() = _error

    protected open fun <T> load(
        loading: LoadingCounter? = loadingState,
        call: suspend () -> T,
    ) = viewModelScope.launch {
        loading?.addLoader()

        runCatching { call() }
            .onFailure {
                _error.value = Event(Exception(it))
            }

        loading?.removeLoader()
    }
}
