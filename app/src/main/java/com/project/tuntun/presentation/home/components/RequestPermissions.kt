package com.project.tuntun.presentation.home.components

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermissions() {
    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    val storagePermissionState = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
        rememberPermissionState(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    } else null

    val isCameraGranted = cameraPermissionState.status.isGranted
    val isStorageGranted = storagePermissionState?.status?.isGranted ?: true

    if (!isCameraGranted || !isStorageGranted) {
        LaunchedEffect(cameraPermissionState, storagePermissionState) {
            cameraPermissionState.launchPermissionRequest()
            storagePermissionState?.launchPermissionRequest()
        }
    }
}
