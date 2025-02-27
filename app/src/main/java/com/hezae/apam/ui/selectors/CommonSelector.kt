package com.hezae.apam.ui.selectors

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Favorite
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.hezae.apam.R
import com.hezae.apam.datas.Style
import com.hezae.apam.ui.buttons.StripIconButton


@Composable
fun CommonSelector(modifier: Modifier,title: String,
                       selectedIndex: MutableState<Int>,
                       items: List<String>,
                       icon: ImageVector = Icons.Sharp.Favorite,
                       isDisplaySelect : Boolean = true,
                       onItemChange: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    StripIconButton(
        modifier = modifier,
        text = title,
        onClick = { expanded = !expanded },
        icon = icon,
        isDisPlayContent =  isDisplaySelect,
        content = items[selectedIndex.value]
    )
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
            items.forEach {
                DropdownMenuItem(
                    modifier = Modifier.padding(2.dp).clip(RoundedCornerShape(8.dp)),
                    text = { Text(text = it) },
                    onClick = {
                        selectedIndex.value = items.indexOf(it)
                        onItemChange(selectedIndex.value)
                        expanded = false
                    },
                )
            }
        }
    }
}
