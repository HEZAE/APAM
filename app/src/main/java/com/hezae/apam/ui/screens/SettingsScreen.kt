package com.hezae.apam.ui.screens

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hezae.apam.R
import com.hezae.apam.datas.Style
import com.hezae.apam.tools.ManagerTheme
import com.hezae.apam.tools.ManagerTheme.currentTheme
import com.hezae.apam.ui.activities.SettingsActivity
import com.hezae.apam.ui.selectors.CommonSelector
import com.hezae.apam.ui.selectors.ThemeSelector


@Composable
fun SettingsScreen(
    modifier: Modifier,
) {
    val context  = LocalContext.current
    val themes = listOf(Style.MICA, Style.DARK, Style.TWILIGHT)
    val selectedThemeIndex  =  remember { mutableIntStateOf(themes.indexOf(currentTheme)) }
    val rtcis = listOf(5, 10, 15, 20, 25)
    val selectedRtcIndex  =  remember { mutableIntStateOf(rtcis.indexOf(getPreference(context,"selected_rtc",5))) }
    //分辨率
    val resolutions = listOf(
        "auto",
        "320x240",
        "640x480",
        "1280x720",
        "1920x1080",
    )
    val styles = listOf(
        "梵高",
        "达芬奇",
        "毕加索",
    )
    val selectedStyleIndex  = remember { mutableIntStateOf(styles.indexOf(getPreference(context,"selected_Style","梵高"))) }
    val selectedResolutionIndex  =  remember { mutableIntStateOf(resolutions.indexOf(getPreference(context,"selected_resolution","auto"))) }
    Card(modifier){
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
            IconButton(onClick = {
                (context as SettingsActivity).finish()
            }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
            Spacer(Modifier.width(4.dp))
            Text(text = "设置", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = MaterialTheme.colorScheme.primary)
        }
        CommonSelector(Modifier.fillMaxWidth().padding(horizontal = 10.dp),
            title = "实时转换间隔",
            selectedIndex = selectedRtcIndex ,
            items = rtcis.map { "${it}s" },
            icon = ImageVector.vectorResource(id = R.drawable.ic_flower_one_red),
        ) {
            savePreference(context,"selected_rtc",rtcis[it])
        }
        CommonSelector(Modifier.fillMaxWidth().padding(horizontal = 10.dp),
            title = "风格",
            selectedIndex = selectedStyleIndex ,
            items = styles,
            icon = ImageVector.vectorResource(id = R.drawable.ic_flower_one_red),
            ) {
            savePreference(context,"selected_theme",styles[it])
        }

        CommonSelector(Modifier.fillMaxWidth().padding(horizontal = 10.dp),
            title = "分辨率",
            selectedIndex = selectedResolutionIndex ,
            items = resolutions,
            icon = ImageVector.vectorResource(id = R.drawable.ic_flower_one_red),
            ) {
            savePreference(context,"selected_resolution",resolutions[it])
        }

         CommonSelector(Modifier.fillMaxWidth().padding(horizontal = 10.dp),
             title = "主题",
             selectedIndex = selectedThemeIndex ,
             items = themes.map {  stringResource(it.title) },
             icon = ImageVector.vectorResource(id = R.drawable.ic_flower_one_red),
             ) {
             currentTheme = themes[it]
             savePreference(context,"selected_theme",themes[it].title)
         }
    }


}

fun <T> savePreference(context: Context,key:String, item : T,) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    when (item) {
        is Int -> {
            val editor = sharedPreferences.edit()
            editor.putInt(key, item)
            editor.apply()
        }
        is String -> {
            val editor = sharedPreferences.edit()
            editor.putString(key, item)
            editor.apply()
        }
        is Boolean -> {
            val editor = sharedPreferences.edit()
            editor.putBoolean(key, item)
            editor.apply()
        }
        else -> {
            throw IllegalArgumentException("不支持的数据类型")
        }
    }
}

fun <T> getPreference(context: Context,key:String, defaultValue: T): T {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    return when (defaultValue) {
        is Int -> {
            sharedPreferences.getInt(key, defaultValue) as T
        }
        is String -> {
            sharedPreferences.getString(key, defaultValue) as T
        }
        is Boolean -> {
            sharedPreferences.getBoolean(key, defaultValue) as T
        }
        else -> {
            throw IllegalArgumentException("不支持的数据类型")
        }
    }
}