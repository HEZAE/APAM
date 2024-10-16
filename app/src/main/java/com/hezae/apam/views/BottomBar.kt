package com.hezae.apam.views

import androidx.compose.foundation.layout.height
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
import com.hezae.apam.data.Screen


@Composable
fun BottomBar(currentPage:  Int, onPageSelected: (Int) -> Unit, items : List<Screen>) {
    NavigationBar(
        modifier = Modifier
            .height(60.dp),
        containerColor = Color.Transparent
    ) {
        items.forEachIndexed { index, screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = stringResource((screen.title)), modifier = Modifier.size(22.dp)) },
                selected = index == currentPage,
                onClick = {
                    if (currentPage != index) { // 避免重复导航
                        onPageSelected(index)
                    }
                },
                label = {
                    Text(stringResource((screen.title)))
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




