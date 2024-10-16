package com.hezae.apam.views.camera

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CameraScreen(modifier: Modifier) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()).padding(8.dp)
    ) {
        Text("拍摄页面")
    }
}
