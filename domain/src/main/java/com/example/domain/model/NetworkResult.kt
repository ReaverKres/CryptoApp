package com.example.domain.model
import com.example.domain.model.NetworkResult.Success
import com.example.domain.model.NetworkResult.Error
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * A discriminated union that encapsulates a successful outcome with a value of type [R]
 * or a [E] error.
 */
sealed class NetworkResult<out R, out E> {
    data class Success<out R>(val result: R) : NetworkResult<R, Nothing>()
    data class Error<out E>(val error: E) : NetworkResult<Nothing, E>()

    companion object {
        fun <S> success(value: S): Success<S> = Success(value)
        fun <E> error(error: E): Error<E> = Error(error)
    }
}

/**
 * Calls the specified function [block] and returns its encapsulated result if invocation was successful,
 * catching any [Throwable] exception that was thrown from the block function execution
 * and encapsulating it as a failure.
 */
@Suppress("TooGenericExceptionCaught")
inline fun <R> runCatching(block: () -> R): NetworkResult<R, Throwable> {
    return try {
        NetworkResult.success(block())
    } catch (e: Throwable) {
        NetworkResult.error(e)
    }
}

/**
 * Calls the specified function [block] and returns its encapsulated result if invocation was successful,
 * catching any Throwable exception that was thrown from the block function execution, then mapping it
 * with [errorMapper] and encapsulating it as a failure.
 */
@Suppress("TooGenericExceptionCaught")
inline fun <R, E> runCatching(
    errorMapper: (Throwable) -> E,
    block: () -> R,
): NetworkResult<R, E> {
    return try {
        NetworkResult.success(block())
    } catch (e: Throwable) {
        NetworkResult.error(errorMapper(e))
    }
}

/**
 * Performs the given [action] on the encapsulated value if this instance represents success.
 * Returns the original [NetworkResult] unchanged.
 */
@OptIn(ExperimentalContracts::class)
inline fun <R, E> NetworkResult<R, E>.onSuccess(action: (value: R) -> Unit): NetworkResult<R, E> {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this is Success) {
        action(result)
    }
    return this
}

/**
 * Performs the given [action] on the encapsulated [E] error if this instance represents failure.
 * Returns the original [NetworkResult] unchanged.
 */
@OptIn(ExperimentalContracts::class)
inline fun <R, E> NetworkResult<R, E>.onError(action: (error: E) -> Unit): NetworkResult<R, E> {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this is NetworkResult.Error) {
        action(error)
    }
    return this
}

val <R, E> NetworkResult<R, E>.isSuccess: Boolean
    get() = this is Success

val <R, E> NetworkResult<R, E>.isError: Boolean
    get() = this is Error

fun <R, E> NetworkResult<R, E>.getOrNull() = when (this) {
    is Success -> result
    is Error -> null
}

fun <R, E> NetworkResult<R, E>.getOrElse(defaultValue: () -> R) = when (this) {
    is Success -> result
    is Error -> defaultValue()
}

fun <R, E> NetworkResult<R, E>.getOrThrow() = when (this) {
    is Success -> result
    is Error -> throw IllegalStateException("Get value from error result")
}
