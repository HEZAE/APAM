package com.hezae.apam.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.hezae.apam.R

sealed class Screen(val title: Int, val icon: ImageVector) {
    data object Home : Screen(R.string.Main_Home, Icons.Filled.Home)
    data object Camera : Screen(R.string.Main_Camera, Icons.Filled.AccountBox)
    data object Find : Screen(R.string.Main_Find, Icons.Filled.Search)
    data object  User : Screen(R.string.Main_User, Icons.Filled.Face)
}