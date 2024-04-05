package com.milomobile.bathrooms4tp.presentation.maps

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import arrow.optics.copy
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.milomobile.bathrooms4tp.util.baseLog
import com.milomobile.bathrooms4tp.util.capitalize
import org.koin.androidx.compose.koinViewModel

@Composable
fun BathroomMap(
    viewModel: MapViewModel = koinViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current

    val parkchester = com.google.android.gms.maps.model.LatLng(40.8364722, -073.8587500)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(parkchester, 10f)
    }

    val uiSettings by remember {
        mutableStateOf(MapUiSettings())
    }
    var properties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL, isMyLocationEnabled = true))
    }
    var displayMapTypes by remember {
        mutableStateOf(false)
    }

    DisposableEffect(key1 = viewModel) {
        viewModel.onStart(context)
        onDispose { }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.matchParentSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = uiSettings,
                properties = properties,
                onMapClick = {
                    //On map click, reposition camera
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(it, cameraPositionState.position.zoom)
                },
                onMapLongClick = {
                    baseLog(message = "Long clicked: $it")
                    viewModel.dropPin(it)
                    baseLog(message = "Dropped pins: ${state.droppedPins}")
                }
//                onMyLocationButtonClick = {
//                    baseLog(message = "My Location Tapped")
//                    true
//                }
            ) {
//                Marker(
//                    state = MarkerState(position = parkchester),
//                    title = "Parkchester",
//                    snippet = "Marker in Parkchester"
//                )

                state.userLocation?.let { latLngPosition ->
                    Marker(
                        alpha = 0.75f,
                        state = MarkerState(position = latLngPosition),
                        title = "You",
                        snippet = "Current Location"
                    )
                }

                state.droppedPins.forEachIndexed { index, latLngPosition ->
                    Marker(
                        alpha = 0.5f,
                        state = MarkerState(position = latLngPosition),
                        title = "Dropped Pin #${index + 1}",
                    )
                }

                state.bathroomLocations.forEachIndexed { index, latLngPosition ->
                    Marker(
                        state = MarkerState(position = latLngPosition),
                        title = "Bathroom #${index + 1}",
                    )
                }
            }

            MapTypeOptions(
                displayMapTypes = displayMapTypes,
                onDisplayMapTypeClick = { displayMapTypes = !displayMapTypes },
                properties = properties,
                onMapTypeSelected = { properties = properties.copy(mapType = it) }
            )

            if (state.droppedPins.isNotEmpty()) {
                TextButton(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .background(
                            Color.LightGray.copy(alpha = 0.85f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(8.dp),
                    onClick = { viewModel.clearPins() }) {
                    Text(text = "Clear Pins")
                }
            }
        }
    }
}

@Composable
fun BoxScope.MapTypeOptions(
    displayMapTypes: Boolean,
    onDisplayMapTypeClick: () -> Unit,
    properties: MapProperties,
    onMapTypeSelected: (MapType) -> Unit
) {
    Column(
        modifier = Modifier
            .align(Alignment.TopStart)
            .background(
                Color.LightGray.copy(alpha = 0.85f), shape = RoundedCornerShape(bottomEnd = 25.dp)
            ),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            modifier = Modifier
                .clickable {
                    onDisplayMapTypeClick()
                }
                .padding(12.dp),
            text = "Map Type",
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        AnimatedVisibility(visible = displayMapTypes) {
            Column(
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                MapType.entries.forEach {
                    if (it != MapType.NONE) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = properties.mapType == it,
                                onClick = { onMapTypeSelected(it) }
                            )
                            Text(text = it.name.capitalize())
                        }
                    }
                }
            }
        }
    }
}