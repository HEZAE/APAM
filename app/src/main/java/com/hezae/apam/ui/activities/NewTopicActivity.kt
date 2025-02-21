package com.hezae.apam.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hezae.apam.tools.ManagerTheme
import com.hezae.apam.ui.Themes.APAMTheme
import com.hezae.apam.ui.screens.FindScreens.NewTopicScreen
import com.hezae.apam.viewmodels.TopicViewModel

class NewTopicActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val topicViewModel = TopicViewModel()
            APAMTheme(ManagerTheme.currentTheme) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NewTopicScreen(Modifier.padding(innerPadding), topicViewModel)
                }
            }
        }
    }
}

