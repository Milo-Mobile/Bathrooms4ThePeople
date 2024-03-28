package com.milomobile.bathrooms4tp.presentation.bathroom_list

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
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
import com.milomobile.bathrooms4tp.data.model.bathroom_models.Address
import com.milomobile.bathrooms4tp.data.model.bathroom_models.Bathroom
import com.milomobile.bathrooms4tp.data.model.bathroom_models.OperatingHours
import com.milomobile.bathrooms4tp.data.model.bathroom_models.capitalizeGender
import com.milomobile.bathrooms4tp.data.model.bathroom_models.mapAddress
import com.milomobile.bathrooms4tp.data.model.bathroom_models.mockBathroom
import com.milomobile.bathrooms4tp.data.repository.BathroomRepositoryImpl.Companion.GOOGLE_MAPS_PACKAGE
import com.milomobile.bathrooms4tp.data.repository.BathroomRepositoryImpl.Companion.GOOGLE_MAPS_QUERY
import com.milomobile.bathrooms4tp.presentation.bathroom_list.components.BathroomHours
import com.milomobile.bathrooms4tp.presentation.bathroom_list.components.BathroomImage
import com.milomobile.bathrooms4tp.presentation.bathroom_list.components.BathroomNotes
import com.milomobile.bathrooms4tp.presentation.bathroom_list.components.BathroomProperties

@Composable
fun BathroomDetails(
    bathroom: Bathroom,
    onClose: () -> Unit
) {
    val mapsButtonEnabled = remember {
        mutableStateOf(true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                modifier = Modifier.align(Alignment.CenterStart),
                onClick = onClose
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back_icon)
                )
            }

            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(R.string.bathroom_details),
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(42.dp))
            BathroomProperties(
                asRow = false,
                label = stringResource(R.string.address),
                data = "${bathroom.address.street}\n${bathroom.address.city}, ${bathroom.address.state} - ${bathroom.address.zipcode}",
                spacing = 4.dp,
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BathroomProperties(
                    asRow = false,
                    label = stringResource(R.string.genders),
                    data = bathroom.capitalizeGender() ?: stringResource(R.string.n_a),
                )

                BathroomProperties(
                    asRow = false,
                    label = stringResource(R.string.rating),
                    data = bathroom.roundedRating.toString(),
                )
            }

            MapsButton(
                bathroomAddress = bathroom.address,
                isEnabled = mapsButtonEnabled.value,
                onNoMapsCompatibleApp = {
                    mapsButtonEnabled.value = false
                }
            )

            if (!mapsButtonEnabled.value) {
                Text(
                    text = stringResource(R.string.no_map_app_on_device),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }

            bathroom.imageUrl?.isNotEmpty()?.let { imageUrlNotEmpty ->
                if (imageUrlNotEmpty) { BathroomImage(imageUrl = bathroom.imageUrl) }
            }

            BathroomHours(operatingHours = bathroom.operatingHours)

            bathroom.notes?.isNotEmpty()?.let {notesNotEmpty ->
                if (notesNotEmpty) { BathroomNotes(notes = bathroom.notes) }
            }
        }
    }
}

@Composable
fun MapsButton(
    bathroomAddress: Address,
    isEnabled: Boolean,
    onNoMapsCompatibleApp: () -> Unit
) {
    val context = LocalContext.current
    Spacer(modifier = Modifier.height(24.dp))
    Button(
        onClick = {
            val addressUri = Uri.encode(bathroomAddress.mapAddress())
            val geoLocation = Uri.parse("${GOOGLE_MAPS_QUERY}$addressUri")
            val intent = Intent(Intent.ACTION_VIEW, geoLocation)
            intent.setPackage(GOOGLE_MAPS_PACKAGE)
            intent.resolveActivity(context.packageManager)?.let {
                context.startActivity(intent)
            } ?: onNoMapsCompatibleApp()
        },
        enabled = isEnabled
    ) {
        Text(text = stringResource(R.string.open_in_maps))
        Icon(
            imageVector = Icons.Filled.Place,
            contentDescription = stringResource(R.string.map_pin_icon)
        )
    }
    Spacer(modifier = Modifier.height(if (!isEnabled) 12.dp else 24.dp ))
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