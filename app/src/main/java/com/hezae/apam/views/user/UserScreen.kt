package com.hezae.apam.views.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hezae.apam.R
import com.hezae.apam.data.Style
import com.hezae.apam.ui.buttons.StripButton
import com.hezae.apam.ui.other.Avatar

@Composable
fun UserScreen(modifier: Modifier, selectedTheme: Style, onThemeChange: (Style) -> Unit) {
    Column(modifier = modifier.padding(8.dp)) {
        Avatar()
        Spacer(modifier = Modifier.height(16.dp))
        ThemeSelector(selectedTheme) { newTheme ->
            onThemeChange(newTheme) // 使用回调更新主题
        }
        StripButton("设置", onClick = {})
        StripButton("关于", onClick = {})
    }
}
@Composable
fun ThemeSelector(selectedTheme: Style, onThemeChange: (Style) -> Unit) {
    var expanded by remember { mutableStateOf(false) } // 控制下拉菜单的展开状态
    val styles = listOf(Style.MICA,Style.DARK,Style.TWILIGHT)
    Box {
        // 按钮用于触发下拉菜单
        Text(
            text = "${stringResource(R.string.select_theme)}: ${stringResource(selectedTheme.title)}",
            modifier = Modifier
                .padding(8.dp)
                .clickable { expanded = !expanded } // 切换下拉菜单的展开状态
        )
        // 下拉菜单
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }, // 关闭下拉菜单
            modifier = Modifier.padding(1.dp).fillMaxWidth(0.95f),
         //   containerColor = MaterialTheme.colorScheme.background
            ) {
            styles.forEach { themeType -> // 使用 values() 而不是 entries()
                DropdownMenuItem(
                    modifier = Modifier,
                    text = { Text(stringResource(themeType.title)) },
                    onClick = {
                        onThemeChange(themeType) // 选择主题
                        expanded = false // 选择后关闭菜单
                    },
                    )
            }
        }
    }
}