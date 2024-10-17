package com.hezae.apam.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    var counter by mutableIntStateOf(0)
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var token by mutableStateOf("")
    var isLoggedIn by mutableStateOf(false)
    var isRememberPassword by mutableStateOf(false)
    var isAutoLogin by mutableStateOf(false)
}