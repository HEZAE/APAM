package com.hezae.apam.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hezae.apam.datas.Style
import com.hezae.apam.tools.ManagerTheme
import com.hezae.apam.ui.screens.SettingsScreen
import com.hezae.apam.ui.topbars.SettingsTopBar
import com.hezae.apam.ui.Themes.APAMTheme

class SettingsActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            APAMTheme(ManagerTheme.currentTheme) {
                Scaffold(Modifier.fillMaxSize()
                ) { innerPadding ->
                    SettingsScreen(modifier = Modifier.padding(innerPadding).fillMaxSize())
                }
            }
        }
    }
}
