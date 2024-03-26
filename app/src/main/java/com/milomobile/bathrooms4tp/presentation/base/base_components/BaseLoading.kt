package com.milomobile.bathrooms4tp.presentation.base.base_components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun BaseLoading(
    strokeWidth: Dp,
    color: Color = Color.Blue,
    size: Dp,
    fullscreen: Boolean,
    fullscreenBackgroundColor: Color? = null,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "inf_transition")
    val animatedAlpha = infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
                0.25f at 0 with LinearEasing
                1f at 500 with LinearEasing
                0.25f at 1000 with LinearEasing
            },
            repeatMode = RepeatMode.Reverse
        ), label = "anim_alpha"
    )

    val modifier = fullscreenBackgroundColor?.let {
        Modifier
            .fillMaxSize()
            .background(color = it)
    } ?: Modifier.fillMaxSize()


    when (fullscreen) {
        true -> Box(
            modifier = modifier
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(size)
                    .align(Alignment.Center)
                    .alpha(animatedAlpha.value),
                strokeWidth = strokeWidth,
                color = color
            )
        }

        false -> CircularProgressIndicator(
            modifier = Modifier
                .size(size)
                .alpha(animatedAlpha.value),
            strokeWidth = strokeWidth,
            color = color
        )
    }
}

@Composable
fun LargeLoading(
    color: Color = Color.Blue,
    fullscreen: Boolean,
    fullscreenBackgroundColor: Color? = null
) {
    BaseLoading(
        color = color,
        fullscreen = fullscreen,
        fullscreenBackgroundColor = fullscreenBackgroundColor,
        size = 72.dp,
        strokeWidth = 8.dp
    )
}