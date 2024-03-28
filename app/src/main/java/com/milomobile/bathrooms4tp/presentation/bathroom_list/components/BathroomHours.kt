package com.milomobile.bathrooms4tp.presentation.bathroom_list.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.milomobile.bathrooms4tp.R
import com.milomobile.bathrooms4tp.data.model.bathroom_models.OperatingHours

@Composable
fun BathroomHours(
    operatingHours: OperatingHours
) {
    Column(
        modifier = Modifier
            .padding(vertical = 24.dp)
    ) {
        BathroomProperties(
            label = stringResource(R.string.operating_hours),
            data = operatingHours,
            verticalSpacerHeight = 8.dp
        )
    }
}