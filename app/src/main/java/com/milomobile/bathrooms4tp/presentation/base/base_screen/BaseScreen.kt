package com.milomobile.bathrooms4tp.presentation.base.base_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.milomobile.bathrooms4tp.presentation.base.ErrorHandling
import com.milomobile.bathrooms4tp.presentation.base.base_components.ErrorDialog
import com.milomobile.bathrooms4tp.presentation.base.base_components.LargeLoading
import kotlinx.coroutines.delay

@Composable
fun BaseScreen(
    loadingTrigger: Boolean,
    errorHandling: ErrorHandling?,
    content: @Composable () -> Unit
) {
    var displayLoading by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = loadingTrigger) {
        displayLoading = if (loadingTrigger) {
            true
        } else {
            delay(750)
            false
        }
    }

    Scaffold {
        Box(modifier = Modifier.padding(it)) {
            content()


            AnimatedVisibility(visible = displayLoading, enter = expandVertically(), exit = shrinkVertically() + fadeOut()) {
                LargeLoading(fullscreen = true, fullscreenBackgroundColor = Color.White)
            }
        }

        errorHandling?.let { safeErrorHandling ->
            ErrorDialog(errorHandling = safeErrorHandling)
        }
    }
}