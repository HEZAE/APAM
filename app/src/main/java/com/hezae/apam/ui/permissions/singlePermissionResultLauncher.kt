package com.hezae.apam.ui.permissions

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun SinglePermission(
    input: String,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    val singlePermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                onPermissionGranted()
            } else {
                onPermissionDenied()
            }
        }
    )
    LaunchedEffect(Unit) {
        singlePermissionResultLauncher.launch(input)
    }
}