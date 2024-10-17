package com.hezae.apam.ui.themes.defaultTheme.Mica

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val MicaDarkColorScheme = darkColorScheme(
    primary = Color(0xFF90CAF9),              // 明亮的蓝色（较浅）
    onPrimary = Color.Black,                   // 在主要颜色上的黑色文本
    primaryContainer = Color(0xFF0D47A1),     // 主要容器的深蓝色
    onPrimaryContainer = Color.White,          // 在主要容器上的白色文本
    inversePrimary = Color(0xFF76FF03),        // 反向主要颜色
    secondary = Color(0xFFB2EBF2),            // 柔和的天蓝色（较浅）
    onSecondary = Color.Black,                 // 在次要颜色上的黑色文本
    secondaryContainer = Color(0xFF006064),    // 次要容器的深天蓝色
    onSecondaryContainer = Color.White,        // 在次要容器上的白色文本
    tertiary = Color(0xFFB2EBF2),              // 柔和的浅蓝色（与次要颜色相同）
    onTertiary = Color.Black,                  // 在三次颜色上的黑色文本
    tertiaryContainer = Color(0xFF80DEEA),     // 三次容器的较浅蓝色
    onTertiaryContainer = Color.Black,         // 在三次容器上的黑色文本
    background = Color(0xFF1E1E1E),            // 应用程序背景颜色，偏深色
    onBackground = Color.White,                 // 在背景上的白色文本
    surface = Color(0xFF263238),               // 表面颜色，偏暗
    onSurface = Color.White,                    // 在表面上的白色文本
    surfaceVariant = Color(0xFF37474F),        // 表面变体颜色，偏深
    onSurfaceVariant = Color.White,             // 在表面变体上的白色文本
    surfaceTint = Color(0xFF90CAF9),           // 表面颜色的 tint
    inverseSurface = Color(0xFFECEFF1),        // 反向表面颜色
    inverseOnSurface = Color.Black,             // 在反向表面上的黑色文本
    error = Color(0xFFD32F2F),                 // 错误颜色
    onError = Color.White,                      // 在错误颜色上的白色文本
    errorContainer = Color(0xFFF9BBAE),        // 错误容器颜色
    onErrorContainer = Color.White,             // 在错误容器上的白色文本
    outline = Color(0xFFB0B0B0),                // 边框颜色
    outlineVariant = Color(0xFFD0D0D0),         // 边框变体颜色
    scrim = Color(0xFF000000).copy(alpha = 0.32f), // 模糊背景颜色
    surfaceBright = Color(0xFF37474F),         // 明亮的表面颜色
    surfaceContainer = Color(0xFF90CAF9),       // 表面容器颜色
    surfaceContainerHigh = Color(0xFFB3E5FC),   // 较高的表面容器颜色
    surfaceContainerHighest = Color(0xFF212121), // 最高优先级的表面容器颜色
    surfaceContainerLow = Color(0xFF424242),    // 较低优先级的表面容器颜色
    surfaceContainerLowest = Color(0xFFFAFAFA), // 最低优先级的表面容器颜色
    surfaceDim = Color(0xFFB0B0B0).copy(alpha = 0.2f) // 暗淡的表面颜色
)



val MicaLightColorScheme = lightColorScheme(
    primary = Color(0xFF3EA5F3),            // 明亮的蓝色
    onPrimary = Color.White,                 // 在主要颜色上的白色文本
    primaryContainer = Color(0xFFBBDEFB),   // 主要容器的较浅蓝色
    onPrimaryContainer = Color.White,        // 在主要容器上的白色文本
    inversePrimary = Color(0xFF004D40),     // 反向主要颜色
    secondary = Color(0xFF81D4FA),          // 柔和的天蓝色
    onSecondary = Color.White,               // 在次要颜色上的白色文本
    secondaryContainer = Color(0xFF40C4FF),  // 次要容器的较浅天蓝色
    onSecondaryContainer = Color.White,      // 在次要容器上的白色文本
    tertiary = Color(0xFFBBDEFB),           // 柔和的浅蓝色
    onTertiary = Color.White,                // 在三次颜色上的白色文本
    tertiaryContainer = Color(0xFFE1F5FE),   // 三次容器的极浅蓝色
    onTertiaryContainer = Color.White,       // 在三次容器上的白色文本
    background = Color(0xFFC4DAF1),          // 应用程序背景颜色，偏蓝色
    onBackground = Color.White,               // 在背景上的白色文本
    surface = Color(0xFFE3F2FD),             // 表面颜色
    onSurface = Color.White,                  // 在表面上的白色文本
    surfaceVariant = Color(0xFFBBDEFB),      // 表面变体颜色
    onSurfaceVariant = Color.White,           // 在表面变体上的白色文本
    surfaceTint = Color(0xFFB3E5FC),         // 表面颜色的 tint
    inverseSurface = Color(0xFF1A1A1A),      // 反向表面颜色
    inverseOnSurface = Color.White,           // 在反向表面上的白色文本
    error = Color(0xFFD32F2F),               // 错误颜色
    onError = Color.White,                    // 在错误颜色上的白色文本
    errorContainer = Color(0xFFF9BBAE),      // 错误容器颜色
    onErrorContainer = Color.White,           // 在错误容器上的白色文本
    outline = Color(0xFFB0B0B0),              // 边框颜色
    outlineVariant = Color(0xFFD0D0D0),       // 边框变体颜色
    scrim = Color(0xFF000000).copy(alpha = 0.32f), // 模糊背景颜色
    surfaceBright = Color(0xFFE3F2FD),       // 明亮的表面颜色
    surfaceContainer = Color(0xFFACCAE5),        // 表面容器颜色 ,下拉列表的容器颜色
    surfaceContainerHigh = Color(0xFFB3E5FC), // 较高的表面容器颜色
    surfaceContainerHighest = Color(0xFFFFFFFF), // 最高优先级的表面容器颜色
    surfaceContainerLow = Color(0xFFE1F5FE), // 较低优先级的表面容器颜色
    surfaceContainerLowest = Color(0xFFFAFAFA), // 最低优先级的表面容器颜色
    surfaceDim = Color(0xFFB0B0B0).copy(alpha = 0.2f) // 暗淡的表面颜色
)

