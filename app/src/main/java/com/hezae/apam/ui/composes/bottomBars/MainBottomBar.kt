package com.hezae.apam.ui.composes.bottomBars

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hezae.apam.data.Screen


@Composable
fun BottomBar(currentPage:  Int, onPageSelected: (Int) -> Unit, items : List<Screen>) {
    NavigationBar(
        modifier = Modifier.padding(0.dp).height(53.dp),
        containerColor = Color.Transparent
    ) {
        items.forEachIndexed { index, screen ->
            NavigationBarItem(
                icon = {
                    Column(
                        modifier = Modifier
                            .padding(0.dp) // 可选：添加一些内边距
                    ) {
                        Icon(
                            imageVector = screen.icon, // 确保这是 ImageVector 类型的资源
                            contentDescription = stringResource(id = screen.title),
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = stringResource(id = screen.title),
                            modifier = Modifier,
                            fontSize = 11.sp
                        )
                    }
                },
                selected = index == currentPage,
                onClick = {
                    if (currentPage != index) {
                        onPageSelected(index)
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor =  Color.Gray,
                    selectedTextColor =  MaterialTheme.colorScheme.primary,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}




