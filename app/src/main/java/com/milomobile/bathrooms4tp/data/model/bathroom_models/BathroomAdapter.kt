package com.milomobile.bathrooms4tp.data.model.bathroom_models

import com.google.firebase.firestore.DocumentSnapshot
import com.milomobile.bathrooms4tp.data.adapter.FirestoreAdapter
import com.milomobile.bathrooms4tp.util.baseLog

class BathroomAdapter : FirestoreAdapter<Bathroom> {
    companion object {
        const val LATITUDE_FIELD = "latitude"
        const val LONGITUDE_FIELD = "longitude"
        const val DAYS_FIELD = "days"
        const val HOURS_FIELD = "hours"
        const val OPEN_FIELD = "open"
        const val CLOSE_FIELD = "close"
    }
    override fun documentToModel(document: DocumentSnapshot): Bathroom? {
        return try {
            val rawAddress = document.get(Bathroom::address.name) as Map<*, *>
            val rawCoordinates = rawAddress[Address::coordinates.name] as Map<*, *>
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


            Bathroom(
                address = Address(
                    street = rawAddress[Address::street.name] as? String ?: "",
                    city = rawAddress[Address::city.name] as? String ?: "",
                    state = rawAddress[Address::state.name] as? String ?: "",
                    zipcode = (rawAddress[Address::zipcode.name] as? Long) ?: 0,
                    coordinates = Coordinates(
                        first = rawCoordinates[LATITUDE_FIELD] as? Double ?: 0.0,
                        second = rawCoordinates[LONGITUDE_FIELD] as? Double ?: 0.0
                    )
                ),
                operatingHours = operatingHours,
                genders = document.getString(Bathroom::genders.name),
                rating = document.getDouble(Bathroom::rating.name) ?: 0.0,
                imageUrl = document.getString(Bathroom::imageUrl.name),
                notes = document.getString(Bathroom::notes.name)
            )
        } catch (e: Exception) {
            baseLog(message = "Exception caught when adapting document to bathroom model")
            baseLog(message = "Exception message: ${e.message}")
            null
        }
    }

    override fun modelToDocument(model: Bathroom): Map<String, Any> {
        TODO("Not yet implemented")
    }
}