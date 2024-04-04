package com.milomobile.bathrooms4tp.presentation.base.base_components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import com.milomobile.bathrooms4tp.R

fun Context.sendUserToPermissions() {
    startActivity(
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
    )
}

@ExperimentalPermissionsApi

@Composable
fun Permission(
    permission: String,
    rationale: String,
    rationaleHorizontalPadding: Dp = 8.dp,
    rationaleBackgroundColor: Color = Color.LightGray,
    permissionNotAvailableContent: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    val permissionState = rememberPermissionState(permission)
    PermissionRequired(
        permissionState = permissionState,
        permissionNotGrantedContent = {
            Rationale(
                text = rationale,
                horizontalPadding = rationaleHorizontalPadding,
                backgroundColor = rationaleBackgroundColor,
                onRequestPermission = { permissionState.launchPermissionRequest() }
            )
        },
        permissionNotAvailableContent = permissionNotAvailableContent,
        content = content
    )
}

@Composable
private fun Rationale(
    text: String,
    horizontalPadding: Dp,
    backgroundColor: Color,
    onRequestPermission: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding)
                .shadow(
                    elevation = 5.dp,
                )
                .align(Alignment.Center)
                .clip(RoundedCornerShape(4.dp))
                .background(color = backgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = stringResource(R.string.location_permission_request),
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier.padding(horizontal = 24.dp),
                text = text,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(16.dp))


            Button(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 16.dp),
                onClick = onRequestPermission
            ) {
                Text(
                    text = stringResource(id = R.string.ok),
                    style = TextStyle.Default.copy(
                        fontWeight = FontWeight.W400, fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                    )
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}