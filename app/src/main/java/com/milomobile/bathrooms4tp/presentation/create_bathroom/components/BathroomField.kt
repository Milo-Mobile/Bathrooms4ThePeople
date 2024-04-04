package com.milomobile.bathrooms4tp.presentation.create_bathroom.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.milomobile.bathrooms4tp.presentation.bathroom_list.components.BathroomLabel

@Composable
fun BathroomField(
    modifier: Modifier = Modifier,
    asRow: Boolean,
    label: String,
    field: @Composable () -> Unit,
    spacing: Dp = 12.dp,
) {
    if (asRow) {
        Row(
            modifier = modifier
        ) {
            BathroomLabel(label = label)
            Spacer(modifier = Modifier.width(spacing))
            field()
        }
    } else {
        Column(
            modifier = modifier
        ) {
            BathroomLabel(label = label)
            Spacer(modifier = Modifier.height(spacing))
            field()
        }
    }
}

@Composable
fun BathroomTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onValueSubmit: () -> Unit
) {
    TextField(
        modifier = modifier,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onNext = {
            onValueSubmit()
        }),
        value = value,
        onValueChange = onValueChange
    )
}

@Composable
fun BathroomDigitField(
    modifier: Modifier = Modifier,
    value: Long,
    onValueChange: (Long) -> Unit,
    onValueSubmit: () -> Unit
) {
    TextField(
        modifier = modifier,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onNext = {
            onValueSubmit()
        }),
        value = value.toString(),
        onValueChange = {
            if (it.isNotBlank() && it.isDigitsOnly()) {
                onValueChange(it.toLong())
            }
        }
    )
}