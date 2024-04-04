package com.milomobile.bathrooms4tp.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import arrow.core.Either
import arrow.core.left
import arrow.core.raise.either
import arrow.core.right
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.milomobile.bathrooms4tp.presentation.base.BaseError
import com.milomobile.bathrooms4tp.util.baseLog
import com.milomobile.bathrooms4tp.util.hasLocationPermissions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlin.time.Duration.Companion.seconds

interface LocationRepository {

    //TODO: Update to be a Flow<Either<>> if pragmatic
    suspend fun getDeviceLocationUpdates(): Flow<LatLng?>

    suspend fun getDeviceLastLocation() : Either<BaseError.LocationQueryError, LatLng>

    suspend fun getDeviceCurrentLocation() : Either<BaseError.LocationQueryError, LatLng>
}

class LocationRepositoryImpl(
    private val context: Context,
    private val locationClient: FusedLocationProviderClient
) : LocationRepository {

    /**
     * [getDeviceLocationUpdates] requires either
     * Coarse or Fine location permissions to successfully retrieve location data
     */
    @SuppressLint("MissingPermission")
    override suspend fun getDeviceLocationUpdates(): Flow<LatLng?> = callbackFlow {
        baseLog(message = "getDeviceLocationUpdates started")
//        if (!context.hasLocationPermissions()) {
//            baseLog(message = "hasLocationPermissions null shortcut")
//            trySend(null)
//            return@callbackFlow
//        }

        baseLog(message = "getDeviceLocationUpdates running request")
        //TODO: Setup battery checking here

        val request = LocationRequest.Builder(10000L)
            .setIntervalMillis(10000L)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                baseLog(message = "got location result")
                locationResult.locations.lastOrNull()?.let {
                    baseLog(message = "last location in result: $it")
                    trySend(LatLng(it.latitude, it.longitude))
                }
            }
        }

        locationClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )

        awaitClose {
            baseLog(message = "closing coroutine")
            locationClient.removeLocationUpdates(locationCallback)
        }
    }

    @SuppressLint("MissingPermission")
    override suspend fun getDeviceLastLocation(): Either<BaseError.LocationQueryError, LatLng> =
        Either.catch {
            val locationResult = locationClient.lastLocation
                .addOnFailureListener {
                    it.left()
                }.await()
            LatLng(locationResult.latitude, locationResult.longitude)
        }.mapLeft {
            BaseError.LocationQueryError(it.message ?: "Unknown Error Occurred")
        }

    @SuppressLint("MissingPermission")
    override suspend fun getDeviceCurrentLocation(): Either<BaseError.LocationQueryError, LatLng> =
        Either.catch {
            val locationResult = locationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                null
            ).addOnFailureListener {
                it.left()
            }.await()
            LatLng(locationResult.latitude, locationResult.longitude)
        }.mapLeft {
            BaseError.LocationQueryError(it.message ?: "Unknown Error Occurred")
        }
}