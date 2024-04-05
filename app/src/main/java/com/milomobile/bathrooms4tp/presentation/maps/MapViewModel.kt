package com.milomobile.bathrooms4tp.presentation.maps

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.milomobile.bathrooms4tp.data.model.bathroom_models.mapAddress
import com.milomobile.bathrooms4tp.data.repository.BathroomRepository
import com.milomobile.bathrooms4tp.data.repository.LocationRepository
import com.milomobile.bathrooms4tp.util.baseLog
import kotlinx.coroutines.launch

class MapViewModel(
    private val locationRepository: LocationRepository,
    private val bathroomRepository: BathroomRepository
) : ViewModel() {
    var state by mutableStateOf(MapState())
        private set

    //ONLY PASSING IN CONTEXT FOR TESTING PURPOSES, FINAL IMPL WILL HAVE PROPER SETUP
    fun onStart(context: Context) {
        baseLog(message = "onStart")
        getCurrentLocation()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getBathrooms(context)
        }
    }

    private fun getCurrentLocation() {
        viewModelScope.launch {
            baseLog(message = "Coroutine started")
            locationRepository.getDeviceLocationUpdates().collect {
                baseLog(message = "Collecting latLng updates")
                it?.let {
                    baseLog(message = "User Location: $it")
                    state = state.copy(userLocation = it)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun getBathrooms(context: Context) {
        viewModelScope.launch {
            bathroomRepository.getBathrooms()
                .onLeft {
                    baseLog(message = "Error when loading bathrooms: $it")
                }.onRight { bathrooms ->
                    baseLog(message = "Loaded bathrooms :$bathrooms")
                    val bathroomPositions : MutableList<LatLng> = mutableListOf()
                    bathrooms.forEach {
                        baseLog(message = "Looping through bathroom: ${it.address.street}")
                        getLatLngFromAddress(context, it.address.mapAddress())?.let { latLng ->
                            baseLog(message = "bathroom LatLng: $latLng")
                            bathroomPositions.add(latLng)
                        }
                    }

                    state = state.copy(
                        bathrooms = bathrooms.sortedByDescending { it.rating },
                        bathroomLocations = bathroomPositions
                    )
                }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun getLatLngFromAddress(context: Context, address: String): LatLng? {
        val geocoder = Geocoder(context)
        return try {
            var latLng: LatLng? = null
            //TODO: All addresses are returning null, lets find out why
            geocoder.getFromLocationName(address, 1) {
                if (it.isNotEmpty()) {
                    val location = it[0]
                    latLng = LatLng(location.latitude, location.longitude)
                }
            }
            baseLog(message = "returning geocoded latLng: $latLng")
            latLng
        } catch (e: Exception) {
            baseLog(message = "Failed to get location")
            e.printStackTrace()
            null
        }
    }

    fun dropPin(latLng: LatLng) {
        val mutableList = state.droppedPins.toMutableList()
        mutableList.add(latLng)
        state = state.copy(droppedPins = mutableList.toList())
    }

    fun clearPins() {
        state = state.copy(droppedPins = emptyList())
    }

}