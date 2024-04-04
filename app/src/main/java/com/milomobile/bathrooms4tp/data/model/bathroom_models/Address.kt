package com.milomobile.bathrooms4tp.data.model.bathroom_models

import arrow.optics.optics

@optics data class Address(
    val street: String = "",
    val city: String = "",
    val state: String = "",
    val zipcode: Long = 0,
    val coordinates: Coordinates? = null
) { companion object }

typealias Latitude = Double
typealias Longitude = Double

typealias Coordinates = Pair<Latitude?, Longitude?>

fun Address.mapAddress() =
    "${this.street}, ${this.city}, ${this.state}"
