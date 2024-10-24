package com.hezae.apam.ui.others

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Favorite
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.hezae.apam.R
import com.hezae.apam.datas.Style
import com.hezae.apam.ui.buttons.StripIconButton

@Composable
fun ThemeSelector(selectedTheme: Style, onThemeChange: (Style) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val styles = listOf(Style.MICA, Style.DARK, Style.TWILIGHT)

    StripIconButton(
        text = "${stringResource(R.string.select_theme)} ",
        onClick = { expanded = !expanded },
        icon = Icons.Sharp.Favorite,
    ) {}

    Box(modifier = Modifier.fillMaxWidth()) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .align(Alignment.CenterStart) // 使下拉框居中
                .padding(2.dp).fillMaxWidth(0.98f),
            shape = RoundedCornerShape(6.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
            offset = DpOffset(0.dp, (0).dp)
        ) {
            styles.forEach { themeType ->
                DropdownMenuItem(
                    modifier = Modifier.padding(2.dp).clip(RoundedCornerShape(8.dp)),
                    text = { Text(text = stringResource(themeType.title)) },
                    onClick = {
                        onThemeChange(themeType)
                        expanded = false
                    },
                )
            }
        }
    }
}
