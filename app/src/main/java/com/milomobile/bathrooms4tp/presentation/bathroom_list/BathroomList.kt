package com.milomobile.bathrooms4tp.presentation.bathroom_list

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.navigation.NavController
import com.milomobile.bathrooms4tp.R
import com.milomobile.bathrooms4tp.data.model.bathroom_models.Bathroom
import com.milomobile.bathrooms4tp.data.model.bathroom_models.Bathrooms
import com.milomobile.bathrooms4tp.data.model.bathroom_models.capitalizeGender
import com.milomobile.bathrooms4tp.data.model.bathroom_models.mockBathroom
import com.milomobile.bathrooms4tp.presentation.Route
import com.milomobile.bathrooms4tp.presentation.base.base_screen.BaseScreen
import com.milomobile.bathrooms4tp.presentation.base.mapErrorHandling
import com.milomobile.bathrooms4tp.presentation.bathroom_list.components.BathroomProperties
import com.milomobile.bathrooms4tp.util.baseLog
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun BathroomList(
    navController: NavController,
    viewModel: BathroomListViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsState().value

    //If permissions are not granted, then we need to set state for user to see button to go to settings app
    val locationPermissionRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
                viewModel.setPermissionGrantedAndLoadBathrooms()
                baseLog(message = "Precise location access granted")
            }

            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
                viewModel.setPermissionGrantedAndLoadBathrooms()
                baseLog(message = "Only approximate location access granted")
            }

            else -> {
                viewModel.setPermissionGranted(false)
                viewModel.setLoadingState(false)
                // No location access granted.
                baseLog(message = "No location access granted")
            }
        }
    }

    val checkForLocationPermissions = {
        val coarseLocationPermissionGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        val fineLocationPermissionGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)

        when {
            coarseLocationPermissionGranted == PERMISSION_GRANTED -> {
                viewModel.setPermissionGrantedAndLoadBathrooms()
                baseLog(message = "approximate location access granted")
            }
            fineLocationPermissionGranted == PERMISSION_GRANTED -> {
                viewModel.setPermissionGrantedAndLoadBathrooms()
                baseLog(message = "precise location access granted")
            }
            else -> {
                baseLog(message = "No permissions granted")
                //Request permissions
                locationPermissionRequest.launch(
                    listOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ).toTypedArray()
                )
            }
        }
    }

    DisposableEffect(key1 = Unit) {
        viewModel.onStart()
        onDispose {  }
    }

    LaunchedEffect(key1 = state) {
        if (state.checkLocationPermissions) {
            checkForLocationPermissions()
        }
    }

    BaseScreen(
        loadingTrigger = state.loading,
        errorHandling = state.uiError?.mapErrorHandling(context) {
            viewModel.onClearUIError()
        },
        floatingActionButton = {
            val fabEnabled = remember { mutableStateOf(true) }
            LaunchedEffect(key1 = fabEnabled.value) {
                if (!fabEnabled.value) {
                    delay(250)
                    fabEnabled.value = true
                }
            }
            FloatingActionButton(onClick = {
                if (fabEnabled.value) {
                    navController.navigate(route = Route.CreateBathroomRoute.path)
                    fabEnabled.value = false
                }
            }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Bathroom Button")
            }
        }
    ) {
        AnimatedContent(
            targetState = state.selectedBathroom,
            label = "Bathroom List/Detail"
        ) { selectedBathroom ->
            when (selectedBathroom) {
                null -> {
                    when (state.bathrooms.isEmpty() && !state.loading) {
                        true -> NoBathroomData(onRetryLoad = viewModel::loadBathrooms)
                        false -> Bathrooms(
                            bathrooms = state.bathrooms,
                            onBathroomItemSelected = viewModel::onBathroomItemSelected
                        )
                    }
                }

                else -> {
                    BathroomDetails(
                        bathroom = selectedBathroom,
                        onClose = viewModel::onBathroomDetailsClosed
                    )
                }
            }
        }

        if (state.bathrooms.isNotEmpty() && !state.loading) {
            IconButton(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp),
                onClick = {
                    navController.navigate(route = Route.MapRoute.path)
                }) {
                Icon(imageVector = Icons.Filled.Place, contentDescription = "Map Icon")
            }
        }
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Bathrooms(
    bathrooms: Bathrooms,
    onBathroomItemSelected: (Bathroom) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        stickyHeader {
            Text(
                modifier = Modifier
                    .background(Color.White)
                    .padding(vertical = 16.dp)
                    .fillMaxWidth(),
                text = stringResource(id = R.string.app_name),
                textAlign = TextAlign.Center,
                fontSize = 24.sp
            )
        }
        items(items = bathrooms) {
            BathroomListItem(bathroom = it) { onBathroomItemSelected(it) }
            Spacer(modifier = Modifier
                .background(Color.DarkGray)
                .height(24.dp))
        }
    }
}

@Composable
fun NoBathroomData(
    onRetryLoad: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.no_bathroom_data_to_display)
        )
        Spacer(modifier = Modifier.height(36.dp))
        TextButton(onClick = onRetryLoad) {
            Text(text = stringResource(R.string.reload))
        }
    }
}

@Composable
fun BathroomListItem(bathroom: Bathroom, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .background(Color.LightGray)
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        BathroomProperties(
            asRow = true,
            label = stringResource(id = R.string.address),
            data = bathroom.address.street,
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BathroomProperties(
                asRow = true,
                label = stringResource(id = R.string.genders),
                data = bathroom.capitalizeGender() ?: stringResource(R.string.n_a),
            )

            BathroomProperties(
                asRow = true,
                label = stringResource(id = R.string.rating),
                data = bathroom.roundedRating.toString(),
            )
        }
    }
}

@Preview
@Composable
fun PreviewBathroomListItem() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        BathroomListItem(bathroom = mockBathroom()) { }
    }
}