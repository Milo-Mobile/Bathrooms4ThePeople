package com.milomobile.bathrooms4tp.presentation.bathroom_list

import com.milomobile.bathrooms4tp.data.model.bathroom_models.Bathrooms
import com.milomobile.bathrooms4tp.presentation.base.BaseError

data class BathroomListState(
    val bathrooms: Bathrooms = listOf(),
    val uiError: BaseError? = null,
    val loading: Boolean = false,
)