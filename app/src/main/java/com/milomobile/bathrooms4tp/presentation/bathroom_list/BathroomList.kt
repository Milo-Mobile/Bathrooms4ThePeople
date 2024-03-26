package com.milomobile.bathrooms4tp.presentation.bathroom_list

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.milomobile.bathrooms4tp.R
import com.milomobile.bathrooms4tp.data.model.bathroom_models.Bathroom
import com.milomobile.bathrooms4tp.data.model.bathroom_models.capitalizeGender
import com.milomobile.bathrooms4tp.data.model.bathroom_models.mockBathroom
import com.milomobile.bathrooms4tp.presentation.base.base_screen.BaseScreen
import com.milomobile.bathrooms4tp.presentation.base.mapErrorHandling
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
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
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            stickyHeader {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.app_name),
                    textAlign = TextAlign.Center
                )
            }
            items(items = state.bathrooms) {
                BathroomListItem(bathroom = it) {
                    viewModel.onBathroomItemSelected(it)
                }
            }
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
            Text(text = stringResource(R.string.rating, bathroom.rating))
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