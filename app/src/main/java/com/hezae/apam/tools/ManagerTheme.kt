package com.hezae.apam.tools

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.hezae.apam.datas.Style

object ManagerTheme {
    var currentTheme: Style by mutableStateOf(Style.MICA)
    // 初始化时获取主题偏好设置
    fun init(context: Context) {
        getThemePreference(context)
    }

    // 获取主题偏好设置
    private fun getThemePreference(context: Context) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val themeName = sharedPreferences.getInt("selected_theme", Style.MICA.title) // 默认主题
        currentTheme = when (themeName) {
            Style.MICA.title -> Style.MICA
            Style.DARK.title -> Style.DARK
            Style.TWILIGHT.title -> Style.TWILIGHT
            else -> Style.MICA
        }
    }
}