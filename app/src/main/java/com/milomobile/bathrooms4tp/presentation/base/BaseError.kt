package com.milomobile.bathrooms4tp.presentation.base

import android.content.Context
import com.milomobile.bathrooms4tp.R

data class ErrorHandling(
    val text: String,
    val actionText: String,
    val onAction: () -> Unit
)

interface BaseError

fun BaseError.mapErrorMessage(context: Context) = when (this) {
    is QueryError -> this.message
    else -> context.getString(R.string.unknown_error_occurred)
}

fun BaseError.mapErrorHandling(context: Context, onAction: () -> Unit): ErrorHandling = when (this) {
    is QueryError -> ErrorHandling(
        text = this.mapErrorMessage(context),
        actionText = context.getString(R.string.retry),
        onAction = onAction
    )
    else -> ErrorHandling(
        text = this.mapErrorMessage(context),
        actionText = context.getString(R.string.clear),
        onAction = onAction
    )
}

data class QueryError(val message: String) : BaseError