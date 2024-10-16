package com.hezae.apam.views.find

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hezae.apam.viewmodels.MainViewModel

@Composable
fun FindScreen(viewModel: MainViewModel, modifier: Modifier) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()).padding(8.dp)
    ) {
        Text("发现页面")
    }
}
