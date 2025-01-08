package com.hezae.apam.ui.Themes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.hezae.apam.datas.Style
import com.hezae.apam.ui.Themes.Dark.DarkStyleDarkColorScheme
import com.hezae.apam.ui.Themes.Dark.DarkStyleLightColorScheme
import com.hezae.apam.ui.Themes.Mica.MicaDarkColorScheme
import com.hezae.apam.ui.Themes.Mica.MicaLightColorScheme
import com.hezae.apam.ui.Themes.Mica.Typography
import com.hezae.apam.ui.Themes.Twi.TwilightDarkColorScheme
import com.hezae.apam.ui.Themes.Twi.TwilightLightColorScheme


@Composable
fun APAMTheme(
    style: Style,
    content: @Composable () -> Unit
) {
    val colorScheme = when (style) {
        Style.MICA -> if (isSystemInDarkTheme()) MicaDarkColorScheme else MicaLightColorScheme
        Style.DARK -> if (isSystemInDarkTheme()) DarkStyleDarkColorScheme else DarkStyleLightColorScheme
        Style.TWILIGHT -> if (isSystemInDarkTheme()) TwilightDarkColorScheme else TwilightLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}


