package com.milomobile.bathrooms4tp.presentation.bathroom_list.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.milomobile.bathrooms4tp.R

@Composable
fun BathroomNotes(
    notes: String
) {
    BathroomProperties(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        asRow = false,
        label = stringResource(R.string.bathroom_notes),
        data = notes,
        spacing = 16.dp,
    )
}