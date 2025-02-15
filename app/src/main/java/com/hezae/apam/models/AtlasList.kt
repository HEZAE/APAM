package com.hezae.apam.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class AtlasLists (
    var atlasList: MutableList<AtlasItem> = mutableListOf(),
    var tag: String = "2025年01月",
    var isAllSelect : MutableState<Boolean> = mutableStateOf(false)
)