package com.example.data.remote.util

import com.example.domain.model.DataFetchError
import com.example.data.services.ApiError
import com.example.domain.model.NetworkResult
import com.haroldadmin.cnradapter.NetworkResponse
import java.io.EOFException
import java.net.HttpURLConnection

fun <Response : Any> NetworkResponse<Response, ApiError>.toResult(): NetworkResult<Response, DataFetchError> =
    toResult { it }

fun <Response : Any, Output : Any> NetworkResponse<Response, ApiError>.toResult(
    resultMapper: (Response) -> Output
): NetworkResult<Output, DataFetchError> = when (this) {
    is NetworkResponse.Success -> {
        NetworkResult.success(resultMapper(body))
    }
    is NetworkResponse.ServerError -> {
        if (code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            NetworkResult.error(DataFetchError.Unauthorized(error))
        } else {
            NetworkResult.error(DataFetchError.ServerError(code ?: -1, error))
        }
    }
    is NetworkResponse.NetworkError -> {
        if (isServerError()) {
            NetworkResult.error(DataFetchError.ServerError(-1, error))
        } else {
            NetworkResult.error(DataFetchError.ConnectionError(error))
        }
    }
    is NetworkResponse.UnknownError -> {
        if (code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            NetworkResult.error(DataFetchError.Unauthorized(error))
        } else {
            NetworkResult.error(DataFetchError.ServerError(code ?: -1, error))
        }
    }
}

private fun<Response, Output> NetworkResponse.NetworkError<Response, Output>.isServerError(): Boolean =
    error is EOFException
