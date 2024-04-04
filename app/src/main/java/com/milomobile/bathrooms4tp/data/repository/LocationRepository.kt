package com.milomobile.bathrooms4tp.data.repository

import android.annotation.SuppressLint
import com.google.android.gms.location.FusedLocationProviderClient
import com.milomobile.bathrooms4tp.util.baseLog

interface LocationRepository {
    //TODO: Update this to return an either so that we can practice error handling
    // How do we want to make use of location data in the system??
    suspend fun getUserLocation()
}

class LocationRepositoryImpl(
    private val locationClient: FusedLocationProviderClient
) : LocationRepository {
    @SuppressLint("MissingPermission")
    override suspend fun getUserLocation() {

        //TODO: Can look at the devices battery state to determine which location methods to use
        // If above 50% battery use currentLocation
        // If below 50% use lastLocation to conserve battery
//        locationClient.lastLocation
//            .addOnSuccessListener {
//                baseLog(message = "Retrieved last location")
//                baseLog(message = "${it.latitude}")
//                baseLog(message = "${it.longitude}")
//                baseLog(message = "${it.extras}")
//                baseLog(message = "${it.describeContents()}")
//            }.addOnFailureListener {
//                baseLog(message = "Failed to retrieve last location")
//            }
    }
}