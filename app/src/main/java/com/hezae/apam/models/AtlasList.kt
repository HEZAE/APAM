package com.hezae.apam.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class AtlasLists (
    var atlasList: MutableList<AtlasItem> = mutableListOf(),
    //日期
    var date: String = "2025年01月",

    var isAllSelect : MutableState<Boolean> = mutableStateOf(false)
)