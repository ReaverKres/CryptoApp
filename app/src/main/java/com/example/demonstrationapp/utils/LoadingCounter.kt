/*
 * Copyright 2019 LWO LLC
 */

package com.example.demonstrationapp.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map

class LoadingCounter {
    private var loaders = 0
    private val loadingState = MutableLiveData<Int>()

    val observable: LiveData<Boolean>
        get() = loadingState.map { it > 0 }

    init {
        loadingState.value = loaders
    }

    fun addLoader() {
        loadingState.value = ++loaders
    }

    fun removeLoader() {
        loadingState.value = --loaders
    }
}
