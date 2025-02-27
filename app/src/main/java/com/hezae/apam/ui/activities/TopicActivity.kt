package com.hezae.apam.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.gson.Gson
import com.hezae.apam.models.shemas.Topic
import com.hezae.apam.tools.ManagerTheme
import com.hezae.apam.ui.Themes.APAMTheme
import com.hezae.apam.ui.screens.FindScreens.TopicScreen
import com.hezae.apam.viewmodels.TopicViewModel

class TopicActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val item =  Gson().fromJson(intent.getStringExtra("topic") ?: "", Topic::class.java)
            val username  = intent.getStringExtra("username") ?: ""
            val tags = Gson().fromJson(intent.getStringExtra("tags") ?: "", Array<Int>::class.java).toList()
            if(item!=null){
                val viewModel: TopicViewModel = viewModel()
                
                viewModel.selectedTopic = item
                APAMTheme(ManagerTheme.currentTheme){
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        TopicScreen(
                            innerPadding = innerPadding,
                            username = username,
                            item = item,
                            tags = tags,
                            viewModel = viewModel, viewModel(),viewModel()
                        )
                    }
                }
            }
        }
    }
}

