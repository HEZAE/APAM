package com.hezae.apam.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hezae.apam.datas.ApiResult
import com.hezae.apam.tools.RetrofitInstance
import kotlinx.coroutines.launch
import java.security.MessageDigest


class  LoginViewModel: ViewModel() {
    val isLoading  = mutableStateOf(false)
    //登录请求
    fun login(username:String, password:String, onFinished: (ApiResult<String>) -> Unit){
        isLoading.value = true
        var result: ApiResult<String>?  = null
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.login(username, password.toMD5())
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        result = response.body()!!
                    }else{
                        result = ApiResult(
                            success = false,
                            code = response.code(),
                            msg = response.message(),
                        )
                    }
                } else {
                    Log.e("登录异常", "请求体为空"+response.message())
                    result = ApiResult(
                        success = false,
                        code = response.code(),
                        msg = response.message(),
                    )
                }
            } catch (e: Exception) {
                Log.e("登录异常", e.message ?: "Unknown error" )
                result = ApiResult(
                    success = false,
                    code = 500,
                    msg = e.message ?: "Unknown error"
                )
            }finally {
                isLoading.value = false
                onFinished(result!!)
            }
        }
    }
    private fun String.toMD5(): String {
        // 创建 MessageDigest 实例，指定算法为 MD5
        val md = MessageDigest.getInstance("MD5")
        // 将字符串转换为字节数组并计算哈希值
        val digest = md.digest(this.toByteArray())
        // 将字节数组转换为十六进制字符串
        return digest.joinToString("") { "%02x".format(it) }
    }
}