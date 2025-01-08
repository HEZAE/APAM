package com.hezae.apam.ui.Themes.Dark

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

 val DarkStyleDarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC),
    secondary = Color(0xFF03DAC5),
    tertiary = Color(0xFF3700B3),
    surface = Color(0xFF121212),
    background = Color(0xFF1E1E1E),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onTertiary = Color.White, onSurface = Color.Gray,              // 黑色，用于表面背景上的内容
    onBackground = Color.Gray            // 黑色，用于应用背景上的内容
)

val DarkStyleLightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE),
    secondary = Color(0xFF03DAC5),
    tertiary = Color(0xFF018786),
    surface = Color(0xFFFFFFFF),
    background = Color(0xFFF5F5F5),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onTertiary = Color.White,
    onSurface = Color.Gray,              // 黑色，用于表面背景上的内容
    onBackground = Color.Gray            // 黑色，用于应用背景上的内容
)
