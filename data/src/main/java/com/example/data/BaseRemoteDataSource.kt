package com.example.data

import com.example.domain.model.Output
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException

/**
 * Base class of Remote API Data source
 * @param retrofit the object of Retrofit instance
 */
abstract class BaseRemoteDataSource constructor(
    private val retrofit: Retrofit
) {

    /**
     * Method to parse the Response of API Service
     * @param T the type of Response
     * @param request
     * @return Output<T> the result of the request with type T
     */
    suspend fun <T> getResponse(
        request: suspend () -> Response<T>,
    ): Output<T> {
        return try {
            println("I'm working in thread ${Thread.currentThread().name}")
            val result = request.invoke()
            return if (result.isSuccessful) {
                Output.success(result.body())
            } else {
                parseError(result)
            }
        } catch (e: Exception) {
            Output.error(e)
        }
    }

    /**
     * Method to convert the error response of API Service to requested type
     * @param response the response of requested api
     * @return the ApiError of the request
     */
    private fun parseError(response: Response<*>): Output.Error {
        val converter =
            retrofit.responseBodyConverter<Output.Error>(Output.Error::class.java, arrayOfNulls(0))
        return try {
            response.errorBody()?.let {
                converter.convert(it)
            } ?: Output.Error(UnknownException())
        } catch (e: IOException) {
            Output.Error(e)
        }
    }
}