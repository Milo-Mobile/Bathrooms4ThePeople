package com.milomobile.bathrooms4tp.presentation.bathroom_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.milomobile.bathrooms4tp.R
import com.milomobile.bathrooms4tp.data.model.bathroom_models.Bathroom
import com.milomobile.bathrooms4tp.data.model.bathroom_models.capitalizeGender
import com.milomobile.bathrooms4tp.data.model.bathroom_models.mapIndexToDayOfWeek
import com.milomobile.bathrooms4tp.data.model.bathroom_models.mockBathroom

@Composable
fun BathroomDetails(
    bathroom: Bathroom,
    onClose: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back Icon"
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Bathroom Details",
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {}) {}//TODO: Use better strategy here than an empty icon for spacing
        }
        Spacer(modifier = Modifier.height(42.dp))
        Text(text = stringResource(R.string.address, bathroom.address.street))
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "${bathroom.address.city}, ${bathroom.address.state} - ${bathroom.address.zipcode}"
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
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

        //Image
        bathroom.imageUrl?.let {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
            ) {
                val contentScale = remember {
                    mutableStateOf(ContentScale.Fit)
                }
                AsyncImage(
                    modifier = Modifier.fillMaxWidth(),
                    model = it,
                    contentScale = contentScale.value,
                    contentDescription = "Bathroom Image",
                    error = painterResource(id = R.drawable.baseline_error_24),
                    placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                    onError = { contentScale.value = ContentScale.Fit },
                    onSuccess = { contentScale.value = ContentScale.FillWidth }
                )
            }
        }

        //Operating Hours
        bathroom.operatingHours.let {
            Column(
                modifier = Modifier
                    .padding(vertical = 24.dp)
            ) {
                Text(text = stringResource(R.string.operating_hours), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                it.forEachIndexed { index, pair ->
                    Row(
                        modifier = Modifier.padding(vertical = 6.dp)
                    ) {
                        Text(text = index.mapIndexToDayOfWeek(context = context))
                        Spacer(modifier = Modifier.weight(1f))

                        pair?.let {
                            Text(text = "${pair.first} - ${pair.second}")
                        } ?: Text(text = stringResource(id = R.string.n_a))
                    }
                }
            }
        }

        bathroom.notes?.let {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            ) {
                Text(text = stringResource(R.string.bathroom_notes), fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = it)
            }
        }
    }
}


@Preview
@Composable
fun PreviewBathroomDetails() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        BathroomDetails(
            mockBathroom().copy(
                notes = "1. Move here\n\n2. Move there\n\n3. Now do the Harlem Shake",
                imageUrl = null
            )
        ) {}
    }
}