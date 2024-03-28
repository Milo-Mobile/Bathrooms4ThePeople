package com.milomobile.bathrooms4tp.data.model.bathroom_models

import java.util.Locale

typealias Bathrooms = List<Bathroom>

data class Bathroom(
    val address: Address = Address(),
    val operatingHours: OperatingHours = listOf(),
    val genders: String? = null,
    val rating: Double = 0.0,
    val imageUrl: String? = null,
    val notes: String? = null,
)

typealias OperatingHours =  List<Pair<String, String>?>

enum class DayOfWeek {//Let's map this via index position
    Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday
}

fun Bathroom.capitalizeGender() =
    this.genders?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }


fun mockBathroom() = Bathroom(
    address = Address(
        street = "5th Avenue",
        city = "New York City",
        state = "New York",
        zipcode = 10462,
        coordinates = null
    ), operatingHours = listOf(
        Pair("08:00", "19:00"),
        Pair("08:00", "19:00"),
        Pair("08:00", "19:00"),
        Pair("08:00", "19:00"),
        Pair("08:00", "19:00"),
        Pair("08:00", "19:00"),
        Pair("08:00", "19:00"),
    ), genders = "Male", rating = 4.5, imageUrl = null, notes = null
)