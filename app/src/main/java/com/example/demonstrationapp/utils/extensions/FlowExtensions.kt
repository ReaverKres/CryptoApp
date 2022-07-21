package com.example.demonstrationapp.utils.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

fun <T> Fragment.collectWhenStarted(flow: Flow<T>, collect: suspend (T) -> Unit) {
    lifecycleScope.launchWhenStarted {
        flow.collectLatest(collect)
    }
}