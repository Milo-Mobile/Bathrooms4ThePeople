package com.milomobile.bathrooms4tp.presentation.bathroom_list.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.milomobile.bathrooms4tp.R
import com.milomobile.bathrooms4tp.data.model.bathroom_models.mapIndexToDayOfWeek

@Composable
fun BathroomProperties(
    modifier: Modifier = Modifier,
    asRow: Boolean,
    label: String,
    data: String,
    spacing: Dp = 12.dp,
) {
    if (asRow) {
        Row(
            modifier = modifier
        ) {
            BathroomLabel(label = label)
            Spacer(modifier = Modifier.width(spacing))
            Text(text = data)
        }
    } else {
        Column(
            modifier = modifier
        ) {
            BathroomLabel(label = label)
            Spacer(modifier = Modifier.height(spacing))
            Text(text = data)
        }
    }

}

@Composable
fun BathroomProperties(
    label: String,
    data: List<Pair<String, String>?>,
    verticalSpacerHeight: Dp = 12.dp,
) {
    val context = LocalContext.current
    BathroomLabel(label = label)
    Spacer(modifier = Modifier.height(verticalSpacerHeight))
    data.forEachIndexed { index, pair ->
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

@Composable
fun BathroomLabel(
    label: String
) {
    Text(text = "${label}:", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
}