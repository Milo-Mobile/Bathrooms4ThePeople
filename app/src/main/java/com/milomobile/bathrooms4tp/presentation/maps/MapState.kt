package com.milomobile.bathrooms4tp.presentation.maps

import com.google.android.gms.maps.model.LatLng
import com.milomobile.bathrooms4tp.data.model.bathroom_models.Bathrooms

data class MapState (
    val userLocation: LatLng? = null,
    val droppedPins: List<LatLng> = emptyList(),
    val bathrooms: Bathrooms = emptyList(),
    val bathroomLocations: List<LatLng> = emptyList()
)