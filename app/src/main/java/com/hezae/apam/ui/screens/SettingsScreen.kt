package com.hezae.apam.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hezae.apam.datas.Style
import com.hezae.apam.ui.buttons.StripIconButton
import com.hezae.apam.ui.others.ThemeSelector
import com.hezae.apam.ui.texts.IconText


@Composable
fun SettingsScreen(
    modifier: Modifier,
    selectedTheme: Style,
    onThemeChange: (Style) -> Unit
) {
    Column(modifier = modifier.padding(4.dp)) {
        ThemeSelector(selectedTheme = selectedTheme )
        { newTheme ->
            onThemeChange(newTheme) // 使用回调更新主题
        }
        StripIconButton(
            modifier = Modifier,
            text = "切换用户",
            icon = Icons.Sharp.Build,
            onClick = { }
        ){}

        IconText()
    }
}