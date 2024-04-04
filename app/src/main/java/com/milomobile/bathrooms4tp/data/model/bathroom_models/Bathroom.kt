package com.milomobile.bathrooms4tp.data.model.bathroom_models

import android.content.Context
import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.zipOrAccumulate
import arrow.optics.optics
import com.milomobile.bathrooms4tp.R
import java.util.Locale
import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.round
import com.milomobile.bathrooms4tp.data.model.bathroom_models.Bathroom.MissingCriticalBathroomData.*

typealias Bathrooms = List<Bathroom>

@optics data class Bathroom(
    val address: Address = Address(),
    val operatingHours: OperatingHours? = listOf(),
    val genders: String? = null,
    val rating: Double? = null,
    val imageUrl: String? = null,
    val notes: String? = null,
) {
    companion object {
        const val ZIPCODE_MIN_LENGTH = 5
    }
    sealed interface MissingCriticalBathroomData {
        data object InvalidStreet : MissingCriticalBathroomData
        data object InvalidCity : MissingCriticalBathroomData
        data object InvalidState : MissingCriticalBathroomData
        data object InvalidZipcode : MissingCriticalBathroomData
    }

    /**
     * [validateCriticalDataFailFirst] validate data with a fail first approach
     * The first property that is invalid will cause the Left (Error) of the Either to return
     * If Unit/Right returns, then the validation was a success
     *
     * We lose out on validating all data, so users would have to fix their inputs one at a time
     * Not using this method currently, this is only for demonstration purposes
     */
    fun validateCriticalDataFailFirst() : Either<MissingCriticalBathroomData, Unit> = either {
        ensure(this@Bathroom.address.street.isNotBlank()) { InvalidStreet }
        ensure(this@Bathroom.address.city.isNotBlank()) { InvalidCity }
        ensure(this@Bathroom.address.state.isNotBlank()) { InvalidState }
        ensure(this@Bathroom.address.zipcode.length() > ZIPCODE_MIN_LENGTH) { InvalidZipcode }
    }

    /**
     * [validateCriticalDataAccumulate] validate data with an accumulation approach
     * Whenever a property is invalid, it will be added to a [NonEmptyList] of [MissingCriticalBathroomData] errors.
     * The Left (Error) data will not return until all [ensure]/validation blocks have completed
     *
     * We can validate all data at once and return all relevant errors so that the user can fix their inputs and then retry the operation
     */
    fun validateCriticalDataAccumulate() : Either<NonEmptyList<MissingCriticalBathroomData>, Unit> = either {
        zipOrAccumulate(
            { ensure(this@Bathroom.address.street.isNotBlank()) { InvalidStreet } },
            { ensure(this@Bathroom.address.city.isNotBlank()) { InvalidCity } },
            { ensure(this@Bathroom.address.state.isNotBlank()) { InvalidState } },
            { ensure(this@Bathroom.address.zipcode.length() >= ZIPCODE_MIN_LENGTH) { InvalidZipcode } }
        ) { _, _, _, _ -> }
    }

    val roundedRating = rating?.let { round(rating * 10) / 10 }
}

typealias OperatingHours =  List<Pair<String, String>?>

fun Long.length() = when (this) {
    0L -> 1
    else -> log10(abs(toDouble())).toInt() + 1
}

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