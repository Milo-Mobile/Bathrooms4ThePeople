package com.milomobile.bathrooms4tp.data.model.bathroom_models

import android.content.Context
import com.milomobile.bathrooms4tp.R
import java.util.Locale
import kotlin.math.round

typealias Bathrooms = List<Bathroom>

data class Bathroom(
    val address: Address = Address(),
    val operatingHours: OperatingHours = listOf(),
    val genders: String? = null,
    val rating: Double = 0.0,
    val imageUrl: String? = null,
    val notes: String? = null,
) {
    val roundedRating = round(rating * 10) / 10
}

typealias OperatingHours =  List<Pair<String, String>?>

fun Int.mapIndexToDayOfWeek(context: Context) =
    when (this) {
        0 -> "Sunday"
        1 -> "Monday"
        2 -> "Tuesday"
        3 -> "Wednesday"
        4 -> "Thursday"
        5 -> "Friday"
        6 -> "Saturday"
        else -> context.getString(R.string.n_a)
    }

fun Bathroom.capitalizeGender() =
    this.genders?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }

fun Address.mapAddress() =
    "${this.street}, ${this.city}, ${this.state}"

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