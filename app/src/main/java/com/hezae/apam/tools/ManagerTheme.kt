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

    // 保存主题偏好设置
    fun saveThemePreference(context: Context, theme: Style) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("selected_theme", theme.title.toString()).apply() // 保存选中的主题
        currentTheme = theme // 更新当前主题
    }

    // 获取主题偏好设置
    private fun getThemePreference(context: Context) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val themeName = sharedPreferences.getString("selected_theme", Style.MICA.title.toString()) // 默认主题
        currentTheme = when (themeName) {
            Style.MICA.title.toString() -> Style.MICA
            Style.DARK.title.toString() -> Style.DARK
            Style.TWILIGHT.title.toString() -> Style.TWILIGHT
            else -> Style.MICA
        }
    }
}