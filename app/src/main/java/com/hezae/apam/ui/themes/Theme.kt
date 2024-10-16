package com.hezae.apam.ui.themes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.hezae.apam.data.Style
import com.hezae.apam.ui.themes.Dark.DarkStyleDarkColorScheme
import com.hezae.apam.ui.themes.Dark.DarkStyleLightColorScheme
import com.hezae.apam.ui.themes.Mica.MicaDarkColorScheme
import com.hezae.apam.ui.themes.Mica.MicaLightColorScheme
import com.hezae.apam.ui.themes.Mica.Typography
import com.hezae.apam.ui.themes.Twi.TwilightDarkColorScheme
import com.hezae.apam.ui.themes.Twi.TwilightLightColorScheme


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


