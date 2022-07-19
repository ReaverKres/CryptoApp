package com.example.domain.model

/**
 * Generic class for holding success response, error response and loading status
 */
sealed class Output<out R> {
    data class Success<T>(val data: T?) : Output<T>()
    data class Error(val exception: String) : Output<Nothing>()
    object Loading : Output<Nothing>()

    companion object {
        fun <T> success(data: T?): Output<T> {
            return Success(data)
        }

        fun <T> error(error: Exception): Output<T> {
            return Error(error.localizedMessage.orEmpty())
        }

        fun <T> loading(data: T? = null): Output<T> {
            return Loading
        }
    }
}