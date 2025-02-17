package com.hezae.apam.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hezae.apam.tools.ManagerTheme
import com.hezae.apam.ui.Themes.APAMTheme
import com.hezae.apam.ui.screens.LoginScreen
import com.hezae.apam.viewmodels.LoginViewModel

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            APAMTheme(ManagerTheme.currentTheme){
                val loginViewModel: LoginViewModel = viewModel()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginScreen(Modifier, loginViewModel,innerPadding)
                }
            }
        }
    }
}

