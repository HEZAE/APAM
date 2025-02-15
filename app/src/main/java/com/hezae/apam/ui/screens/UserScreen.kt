package com.hezae.apam.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.res.vectorResource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.hezae.apam.R
import com.hezae.apam.tools.ManagerTheme
import com.hezae.apam.ui.Themes.APAMTheme
import com.hezae.apam.ui.activities.LoginActivity
import com.hezae.apam.ui.activities.MainActivity
import com.hezae.apam.ui.activities.SettingsActivity
import com.hezae.apam.ui.buttons.StripIconButton
import com.hezae.apam.ui.others.Avatar
import com.hezae.apam.ui.others.ThemeSelector
import com.hezae.apam.ui.viewmodels.MainViewModel


@Composable
fun UserScreen(modifier: Modifier, mainViewModel: MainViewModel) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(5.dp),
    ) {
        Avatar(mainViewModel = mainViewModel)
        Spacer(modifier = Modifier.height(16.dp))

        StripIconButton(
            modifier = Modifier.padding(horizontal = 10.dp),
            text = "设置",
            icon = ImageVector.Companion.vectorResource(id = R.drawable.ic_aloe),
            onClick = {
                val intent = Intent(context, SettingsActivity::class.java)
                context.startActivity(intent)
            })
        ThemeSelector(Modifier.padding(horizontal = 10.dp),ManagerTheme.currentTheme,ImageVector.Companion.vectorResource(id = R.drawable.ic_flower_one_red),){
            ManagerTheme.currentTheme = it
        }
        StripIconButton(
            modifier = Modifier.padding(horizontal = 10.dp),
            text = "注销",
            icon = ImageVector.Companion.vectorResource(id = R.drawable.ic_leaf),
            onClick = {
                context.getSharedPreferences("user_info", Context.MODE_PRIVATE).edit().remove("token").apply()
                val intent = Intent(context, LoginActivity::class.java)
                context.startActivity(intent)
                (context as MainActivity).finish()
            })
        StripIconButton(
            modifier = Modifier.padding(horizontal = 10.dp),
            text = "关于",
            icon = ImageVector.Companion.vectorResource(id = R.drawable.ic_flower_sun),
            onClick = {
            })
    }
}

