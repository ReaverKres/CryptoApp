package com.example.domain.model

/**
 * Generic class for holding success response, error response and loading status
 */
sealed class OutputState<out R> {
    data class Success<T>(val data: T?) : OutputState<T>()
    data class Error(val exception: String) : OutputState<Nothing>()
    object Loading : OutputState<Nothing>()

    companion object {
        fun <T> success(data: T?): OutputState<T> {
            return Success(data)
        }

        fun <T> error(error: Exception): OutputState<T> {
            return Error(error.localizedMessage.orEmpty())
        }

        fun <T> loading(data: T? = null): OutputState<T> {
            return Loading
        }
    }
}