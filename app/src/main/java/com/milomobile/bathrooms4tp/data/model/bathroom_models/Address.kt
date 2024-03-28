package com.milomobile.bathrooms4tp.data.model.bathroom_models

data class Address(
    val street: String = "",
    val city: String = "",
    val state: String = "",
    val zipcode: Long = -1,
    val coordinates: Coordinates? = null
)

typealias Latitude = Double
typealias Longitude = Double

typealias Coordinates = Pair<Latitude, Longitude>
