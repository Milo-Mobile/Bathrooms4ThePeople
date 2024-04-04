package com.milomobile.bathrooms4tp.presentation

sealed class Route(val path: String) {
    data object ListRoute : Route("list_route")
    data object CreateBathroomRoute : Route("create_bathroom_route")
}