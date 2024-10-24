package com.hezae.apam.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.hezae.apam.ui.activities.SettingsActivity
import com.hezae.apam.ui.buttons.StripIconButton
import com.hezae.apam.ui.others.Avatar

@Composable
fun UserScreen(modifier: Modifier) {
    val context = LocalContext.current
    Column(modifier = modifier.padding(8.dp)) {
        Avatar()
        Spacer(modifier = Modifier.height(16.dp))

        StripIconButton(modifier=Modifier,text="设置", Icons.Default.Settings, onClick = {
            val intent  = Intent(context, SettingsActivity::class.java)
            context.startActivity(intent)
        }){}
        StripIconButton(modifier=Modifier,text="关于", Icons.Default.Info, onClick = {

        }){}
    }
}
