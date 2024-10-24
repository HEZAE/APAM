package com.hezae.apam.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hezae.apam.datas.Post
import com.hezae.apam.tools.RetrofitInstance
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    var counter by mutableIntStateOf(0)
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var token by mutableStateOf("")
    var isLoggedIn by mutableStateOf(false)
    var isRememberPassword by mutableStateOf(false)
    var isAutoLogin by mutableStateOf(false)

    private val _posts = mutableStateListOf<Post>()
    val posts: List<Post> get() = _posts
    fun fetchPosts() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getPosts()
                _posts.addAll(response)
            } catch (e: Exception) {
                // 处理异常
            }
        }
    }
}