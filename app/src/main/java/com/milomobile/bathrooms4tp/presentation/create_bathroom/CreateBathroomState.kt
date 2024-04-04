package com.milomobile.bathrooms4tp.presentation.create_bathroom

import arrow.core.NonEmptyList
import arrow.optics.optics
import com.milomobile.bathrooms4tp.data.model.bathroom_models.Bathroom
import com.milomobile.bathrooms4tp.presentation.base.BaseError

@optics data class CreateBathroomState (
    val newBathroom: Bathroom = Bathroom(),
    val bathroomSubmittedToDatabase: Boolean = false,
    val uiError: BaseError? = null,
    val loading: Boolean = false,
    val fieldErrors: NonEmptyList<Bathroom.MissingCriticalBathroomData>? = null
) { companion object }