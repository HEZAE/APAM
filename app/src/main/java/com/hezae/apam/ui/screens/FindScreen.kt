package com.hezae.apam.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.hezae.apam.ui.screens.FindScreens.MainTopicScreen
import com.hezae.apam.viewmodels.TopicViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindScreen(modifier: Modifier, viewModel: TopicViewModel) {
    val context = LocalContext.current
    MainTopicScreen(Modifier.fillMaxSize(), viewModel)
}
