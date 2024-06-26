package com.milomobile.bathrooms4tp.data.model.bathroom_models

import arrow.core.Either
import com.google.firebase.firestore.DocumentSnapshot
import com.milomobile.bathrooms4tp.data.adapter.FirestoreAdapter
import com.milomobile.bathrooms4tp.presentation.base.BaseError

class BathroomAdapter : FirestoreAdapter<Bathroom> {
    companion object {
        const val LATITUDE_FIELD = "latitude"
        const val LONGITUDE_FIELD = "longitude"
        const val DAYS_FIELD = "days"
        const val HOURS_FIELD = "hours"
        const val OPEN_FIELD = "open"
        const val CLOSE_FIELD = "close"
    }
    override fun documentToModel(document: DocumentSnapshot): Either<BaseError.AdapterError, Bathroom?> =
        Either.catch {
            val rawAddress = document.get(Bathroom::address.name) as? Map<*, *>
            val rawCoordinates = rawAddress?.get(Address::coordinates.name) as? Map<*, *>
            val rawOperatingHours = document.get(Bathroom::operatingHours.name) as Map<*, *>
            val days = rawOperatingHours[DAYS_FIELD] as? List<*>
            val operatingHours = days?.map { day ->
                val dayMap = day as Map<*, *>
                val hoursMap = dayMap[HOURS_FIELD] as Map<*, *>
                val open = hoursMap[OPEN_FIELD] as? String
                val close = hoursMap[CLOSE_FIELD] as? String

                if (open != null && close != null) {
                    Pair(open, close)
                } else {
                    null
                }
            } ?: listOf()

            val address = Address(
                street = rawAddress?.get(Address::street.name) as? String
                    ?: throw BaseError.AdapterError.NecessaryDataMissing("Missing street property of bathroom on document: ${document.id}"),
                city = rawAddress[Address::city.name] as? String
                    ?: throw BaseError.AdapterError.NecessaryDataMissing("Missing city property of bathroom on document: ${document.id}"),
                state = rawAddress[Address::state.name] as? String
                    ?: throw BaseError.AdapterError.NecessaryDataMissing("Missing state property of bathroom on document: ${document.id}"),
                zipcode = (rawAddress[Address::zipcode.name] as? Long)
                    ?: throw BaseError.AdapterError.NecessaryDataMissing("Missing zipcode property of bathroom on document: ${document.id}"),
                coordinates = Coordinates(
                    first = rawCoordinates?.get(LATITUDE_FIELD) as? Double,
                    second = rawCoordinates?.get(LONGITUDE_FIELD) as? Double
                )
            )

            Bathroom(
                address = address,
                operatingHours = operatingHours,
                genders = document.getString(Bathroom::genders.name),
                rating = document.getDouble(Bathroom::rating.name),
                imageUrl = document.getString(Bathroom::imageUrl.name),
                notes = document.getString(Bathroom::notes.name)
            )
        }.mapLeft {
            when (it) {
                is BaseError.AdapterError.NecessaryDataMissing -> it
                is ClassCastException ->
                    BaseError.AdapterError.UnexpectedTypeCast(it.message ?: "Unable to cast field to expected data type")
                else -> BaseError.AdapterError.UnknownExceptionCaught(it.message ?: "Unknown exception caught")
            }
        }

    override fun modelToDocument(model: Bathroom): Either<BaseError.AdapterError, Map<String, Any?>> =
        Either.catch {
            mapOf(
                Bathroom::address.name to mapOf(
                    Address::street.name to model.address.street,
                    Address::city.name to model.address.city,
                    Address::state.name to model.address.state,
                    Address::zipcode.name to model.address.zipcode,
                    Address::coordinates.name to model.address.coordinates
                ),
                Bathroom::genders.name to model.genders,
                Bathroom::imageUrl.name to model.imageUrl,
                Bathroom::notes.name to model.notes,
                Bathroom::operatingHours.name to model.operatingHours,
                Bathroom::rating.name to model.rating,
            )
        }.mapLeft {
            BaseError.AdapterError.UnknownExceptionCaught(it.message ?: "Foo")
        }
}