package com.milomobile.bathrooms4tp.presentation.bathroom_list.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.milomobile.bathrooms4tp.R

@Composable
fun BathroomImage(
    imageUrl: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
    ) {
        val contentScale = remember {
            mutableStateOf(ContentScale.Fit)
        }
        val imageLoadingFailed = remember {
            mutableStateOf(false)
        }
        AsyncImage(
            modifier = Modifier.fillMaxWidth(),
            model = imageUrl,
            contentScale = contentScale.value,
            contentDescription = "Bathroom Image",
            error = painterResource(id = R.drawable.baseline_error_24),
            placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
            onError = {
                contentScale.value = ContentScale.Fit
                imageLoadingFailed.value = true
            },
            onSuccess = {
                contentScale.value = ContentScale.FillWidth
            }
        )

        if (imageLoadingFailed.value) {
            Text(text = stringResource(R.string.unable_to_load_bathroom_images))
        }
    }
}