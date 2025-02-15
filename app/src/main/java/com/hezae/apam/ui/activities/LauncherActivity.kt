package com.hezae.apam.ui.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hezae.apam.R
import com.hezae.apam.tools.ManagerTheme
import com.hezae.apam.ui.buttons.SkipButton
import com.hezae.apam.ui.Themes.APAMTheme
import kotlinx.coroutines.delay

class LauncherActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ManagerTheme.init(this)
        setContent {
            val selectedTheme = ManagerTheme.currentTheme
            APAMTheme(style = selectedTheme) {
                Scaffold{ innerPadding ->
                    Greeting(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    @Composable
    fun Greeting(modifier: Modifier = Modifier) {
        var seconds by remember { mutableIntStateOf(3) }
        var isNavigated by remember { mutableStateOf(false) }
        val context = LocalContext.current

        Box(
            modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        ) {
            // 跳过按钮位于右上角
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SkipButton(
                    seconds = seconds,
                    onClick = {
                        if (!isNavigated) {
                            isNavigated = true
                            navigateToMain(context)
                        }
                    }
                )
            }
            Row(
                modifier = Modifier .fillMaxWidth().align(Alignment.Center),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,

                ) {
                Text(stringResource(R.string.app_name))
            }
        }

        // 倒计时逻辑
        LaunchedEffect(key1 = seconds, key2 = isNavigated) {
            if (seconds > 0 && !isNavigated) {
                delay(1000L)
                seconds--
            } else if (seconds <= 0 && !isNavigated) {
                isNavigated = true
                navigateToMain(context)
            }
        }
    }

    private fun navigateToMain(context: Context) {
        //从sharedPreferences中获取Token
        val sharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE)
        val token :String? = sharedPreferences.getString("token", null)
        if (token != null) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
            (context as Activity).finish()
        } else {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
            (context as Activity).finish()
        }
    }
}


