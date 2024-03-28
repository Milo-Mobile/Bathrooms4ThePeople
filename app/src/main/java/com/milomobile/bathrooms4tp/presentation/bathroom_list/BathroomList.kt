package com.milomobile.bathrooms4tp.presentation.bathroom_list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.milomobile.bathrooms4tp.R
import com.milomobile.bathrooms4tp.data.model.bathroom_models.Bathroom
import com.milomobile.bathrooms4tp.data.model.bathroom_models.Bathrooms
import com.milomobile.bathrooms4tp.data.model.bathroom_models.capitalizeGender
import com.milomobile.bathrooms4tp.data.model.bathroom_models.mockBathroom
import com.milomobile.bathrooms4tp.presentation.base.base_screen.BaseScreen
import com.milomobile.bathrooms4tp.presentation.base.mapErrorHandling
import org.koin.androidx.compose.koinViewModel

@Composable
fun BathroomList(
    viewModel: BathroomListViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsState().value

    DisposableEffect(key1 = Unit) {
        viewModel.onStart()
        onDispose {  }
    }

    BaseScreen(
        loadingTrigger = state.loading,
        errorHandling = state.uiError?.mapErrorHandling(context) {
            viewModel.onClearUIError()
        }
    ) {
        AnimatedContent(
            targetState = state.selectedBathroom,
            label = "Bathroom List/Detail"
        ) { selectedBathroom ->
            when (selectedBathroom) {
                null -> {
                    when (state.bathrooms.isEmpty()) {
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
                    .fillMaxWidth(),
                text = stringResource(id = R.string.app_name),
                textAlign = TextAlign.Center
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
        Text(text = stringResource(R.string.address, bathroom.address.street))
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(
                    R.string.genders,
                    bathroom.capitalizeGender() ?: stringResource(R.string.n_a)
                )
            )
            Text(text = stringResource(R.string.rating, bathroom.roundedRating))
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