package com.milomobile.bathrooms4tp.presentation.base

import android.content.Context
import com.milomobile.bathrooms4tp.R

data class ErrorHandling(
    val text: String,
    val actionText: String,
    val onAction: () -> Unit
)

sealed class BaseError : Throwable() {
    data class QueryError(override val message: String) : BaseError()

    sealed class AdapterError(override val message: String) : BaseError() {
        //If unexpected type cast then we want to raise an error with error reporting
        data class UnexpectedTypeCast(override val message: String) : AdapterError(message)
        data class NecessaryDataMissing(override val message: String) : AdapterError(message)
        data class UnknownExceptionCaught(override val message: String) : AdapterError(message)
    }

    data class LocationQueryError(override val message: String) : BaseError()
}

fun BaseError.mapErrorMessage() = when (this) {
    is BaseError.QueryError -> this.message
    is BaseError.AdapterError -> this.message
    is BaseError.LocationQueryError -> this.message
}

fun BaseError.mapErrorHandling(context: Context, onAction: () -> Unit): ErrorHandling = when (this) {
    is BaseError.QueryError -> ErrorHandling(
        text = this.mapErrorMessage(),
        actionText = context.getString(R.string.retry),
        onAction = onAction
    )
    is BaseError.AdapterError -> ErrorHandling(
        text = this.mapErrorMessage(),
        actionText = context.getString(R.string.clear),
        onAction = onAction
    )

    is BaseError.LocationQueryError -> ErrorHandling(
        text = this.mapErrorMessage(),
        actionText = context.getString(R.string.clear),
        onAction = onAction
    )
}

