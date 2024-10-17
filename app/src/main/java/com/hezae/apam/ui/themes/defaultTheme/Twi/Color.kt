package com.hezae.apam.ui.themes.defaultTheme.Twi

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val TwilightDarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFA500),              // 暗调的橙色
    onPrimary = Color.Black,                   // 在主要颜色上的黑色文本
    primaryContainer = Color(0xFFFFD700),     // 主要容器的金色
    onPrimaryContainer = Color.Black,          // 在主要容器上的黑色文本
    inversePrimary = Color(0xFFFFF3E0),       // 反向主要颜色
    secondary = Color(0xFFFFC107),            // 暗调的琥珀色
    onSecondary = Color.Black,                 // 在次要颜色上的黑色文本
    secondaryContainer = Color(0xFFFFD54F),    // 次要容器的浅琥珀色
    onSecondaryContainer = Color.Black,        // 在次要容器上的黑色文本
    tertiary = Color(0xFFFFEB3B),             // 暗调的浅黄色
    onTertiary = Color.Black,                  // 在三次颜色上的黑色文本
    tertiaryContainer = Color(0xFFFFF9C4),     // 三次容器的极浅黄色
    onTertiaryContainer = Color.Black,         // 在三次容器上的黑色文本
    background = Color(0xFF212121),           // 应用程序背景颜色，深色
    onBackground = Color.White,                // 在背景上的白色文本
    surface = Color(0xFF424242),               // 表面颜色，暗调
    onSurface = Color.White,                   // 在表面上的白色文本
    surfaceVariant = Color(0xFF616161),        // 表面变体颜色
    onSurfaceVariant = Color.White,            // 在表面变体上的白色文本
    surfaceTint = Color(0xFFFFB74D),          // 表面颜色的 tint
    inverseSurface = Color(0xFF757575),       // 反向表面颜色
    inverseOnSurface = Color.White,            // 在反向表面上的白色文本
    error = Color(0xFFD32F2F),                // 错误颜色
    onError = Color.White,                     // 在错误颜色上的白色文本
    errorContainer = Color(0xFFF9BBAE),       // 错误容器颜色
    onErrorContainer = Color.White,            // 在错误容器上的白色文本
    outline = Color(0xFFB0B0B0),               // 边框颜色
    outlineVariant = Color(0xFFD0D0D0),        // 边框变体颜色
    scrim = Color(0xFF000000).copy(alpha = 0.32f), // 模糊背景颜色
    surfaceBright = Color(0xFF424242),         // 明亮的表面颜色
    surfaceContainer = Color(0xFF303030),      // 表面容器颜色
    surfaceContainerHigh = Color(0xFF616161),  // 较高的表面容器颜色
    surfaceContainerHighest = Color(0xFF424242), // 最高优先级的表面容器颜色
    surfaceContainerLow = Color(0xFF757575),  // 较低优先级的表面容器颜色
    surfaceContainerLowest = Color(0xFFFAFAFA), // 最低优先级的表面容器颜色
    surfaceDim = Color(0xFFB0B0B0).copy(alpha = 0.2f) // 暗淡的表面颜色
)


val TwilightLightColorScheme = lightColorScheme(
    primary = Color(0xFFFFA500),             // 明亮的橙色
    onPrimary = Color.Black,                  // 在主要颜色上的黑色文本
    primaryContainer = Color(0xFFFFD700),    // 主要容器的金色
    onPrimaryContainer = Color.Black,         // 在主要容器上的黑色文本
    inversePrimary = Color(0xFF000000),      // 反向主要颜色
    secondary = Color(0xFFFFC107),           // 明亮的琥珀色
    onSecondary = Color.Black,                // 在次要颜色上的黑色文本
    secondaryContainer = Color(0xFFFFD54F),   // 次要容器的浅琥珀色
    onSecondaryContainer = Color.Black,       // 在次要容器上的黑色文本
    tertiary = Color(0xFFFFEB3B),            // 明亮的浅黄色
    onTertiary = Color.Black,                 // 在三次颜色上的黑色文本
    tertiaryContainer = Color(0xFFFFF9C4),    // 三次容器的极浅黄色
    onTertiaryContainer = Color.Black,        // 在三次容器上的黑色文本
    background = Color(0xFFFFF3E0),          // 应用程序背景颜色
    onBackground = Color.Black,                // 在背景上的黑色文本
    surface = Color(0xFFFFE0B2),              // 表面颜色
    onSurface = Color.Black,                   // 在表面上的黑色文本
    surfaceVariant = Color(0xFFFFCC80),       // 表面变体颜色
    onSurfaceVariant = Color.Black,            // 在表面变体上的黑色文本
    surfaceTint = Color(0xFFFFB74D),          // 表面颜色的 tint
    inverseSurface = Color(0xFF303030),       // 反向表面颜色
    inverseOnSurface = Color.White,            // 在反向表面上的白色文本
    error = Color(0xFFD32F2F),                // 错误颜色
    onError = Color.White,                     // 在错误颜色上的白色文本
    errorContainer = Color(0xFFF9BBAE),       // 错误容器颜色
    onErrorContainer = Color.White,            // 在错误容器上的白色文本
    outline = Color(0xFFB0B0B0),               // 边框颜色
    outlineVariant = Color(0xFFD0D0D0),        // 边框变体颜色
    scrim = Color(0xFF000000).copy(alpha = 0.32f), // 模糊背景颜色
    surfaceBright = Color(0xFFFFE0B2),        // 明亮的表面颜色
    surfaceContainer = Color(0xFFFFF3E0),      // 表面容器颜色
    surfaceContainerHigh = Color(0xFFFFCC80),  // 较高的表面容器颜色
    surfaceContainerHighest = Color(0xFFFFFFFF), // 最高优先级的表面容器颜色
    surfaceContainerLow = Color(0xFFFFF9C4),   // 较低优先级的表面容器颜色
    surfaceContainerLowest = Color(0xFFFAFAFA), // 最低优先级的表面容器颜色
    surfaceDim = Color(0xFFB0B0B0).copy(alpha = 0.2f) // 暗淡的表面颜色
)