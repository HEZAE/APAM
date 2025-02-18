package com.hezae.apam.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hezae.apam.tools.ManagerTheme
import com.hezae.apam.ui.Themes.APAMTheme
import com.hezae.apam.ui.screens.PictureScreen
import com.hezae.apam.viewmodels.PictureViewModel

class PictureActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: PictureViewModel = viewModel()
            //读取意图
            viewModel.album.value.id = intent.getStringExtra("id") ?: ""
            viewModel.album.value.name = intent.getStringExtra("name") ?: ""
            viewModel.album.value.count = intent.getIntExtra("count", 0)
            viewModel.album.value.cover_picture_id = intent.getStringExtra("cover_picture_id") ?: ""

            APAMTheme(ManagerTheme.currentTheme){
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PictureScreen(Modifier.padding(innerPadding),viewModel)
                }
            }
        }
    }
}

