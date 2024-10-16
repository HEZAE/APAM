package com.hezae.apam.views.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hezae.apam.viewmodels.MainViewModel

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize().padding(4.dp)) {
        Text(text = "点击次数: ${viewModel.counter}")
        Button(
            onClick = { viewModel.counter++ },
        ) {
            Text(text = "+")
        }
        Button(
            onClick = { viewModel.counter-- },
        ) {
            Text(text = "-")
        }
    }
}
