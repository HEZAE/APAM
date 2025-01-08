package com.hezae.apam.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.ui.res.vectorResource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hezae.apam.R
import com.hezae.apam.ui.activities.SettingsActivity
import com.hezae.apam.ui.buttons.StripIconButton
import com.hezae.apam.ui.others.Avatar


@Composable
fun UserScreen(modifier: Modifier) {
    val context = LocalContext.current
    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Avatar()
        Spacer(modifier = Modifier.height(16.dp))

        StripIconButton(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = "设置",
            icon = ImageVector.Companion.vectorResource(id = R.drawable.ic_aloe),
            onClick = {
                val intent = Intent(context, SettingsActivity::class.java)
                context.startActivity(intent)
            })
        StripIconButton(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = "注销",
            icon = ImageVector.Companion.vectorResource(id = R.drawable.ic_leaf),
            onClick = {})
        StripIconButton(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = "关于",
            icon = ImageVector.Companion.vectorResource(id = R.drawable.ic_flower_one_red),
            onClick = {
            })
    }
}

