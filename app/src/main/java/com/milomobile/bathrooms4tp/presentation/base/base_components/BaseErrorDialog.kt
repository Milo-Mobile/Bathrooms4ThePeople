package com.milomobile.bathrooms4tp.presentation.base.base_components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.milomobile.bathrooms4tp.presentation.base.ErrorHandling

@Composable
fun ErrorDialog(
    errorHandling: ErrorHandling
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .blur(30.dp)
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val errorDialogShape = RoundedCornerShape(corner = CornerSize(20.dp))
            Column(
                modifier = Modifier
                    .background(
                        Color.White,
                        shape = errorDialogShape
                    )
                    .border(
                        width = 2.dp,
                        color = Color.Black,
                        shape = errorDialogShape
                    )
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = errorHandling.text,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(onClick = errorHandling.onAction) {
                    Text(text = errorHandling.actionText)
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewDisplayError() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(
            brush = Brush.linearGradient(
                listOf(
                    Color.DarkGray,
                    Color.Red,
                    Color.LightGray
                )
            )
        )
    ) {
        ErrorDialog(errorHandling = ErrorHandling("Gojira was here", "Retry") {})
    }
}