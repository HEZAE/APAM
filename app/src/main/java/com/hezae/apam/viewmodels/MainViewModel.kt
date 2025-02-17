package com.hezae.apam.viewmodels

import android.media.session.MediaSession.Token
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hezae.apam.datas.ApiResult
import com.hezae.apam.models.User
import com.hezae.apam.tools.RetrofitInstance
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    var user  = mutableStateOf( User("","","未知","",0,0.0f,0f, 0,0,""))
    private var isLoadingUser = mutableStateOf(false)
    fun getUser(token:String,onFinished : (ApiResult<String>) -> Unit){
        isLoadingUser.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getUserInfo("bearer $token")
                if (response.isSuccessful){
                    if(response.body() != null){
                        val date = response.body()!!.data
                        if(date != null){
                            user.value  = date
                            onFinished(ApiResult(true,200,"",user.value.username))
                        }else{
                            onFinished(ApiResult(false,response.code(),response.message(),""))
                        }
                    }else{
                        onFinished(ApiResult(false,response.code(),response.message(),""))
                    }
                }else{
                    onFinished(ApiResult(false,response.code(),response.message(),""))
                }
            }catch (e:Exception){
                Log.e("MainViewModel",e.message.toString())
                onFinished(ApiResult(false,500,e.message.toString(),""))
            }finally {
                isLoadingUser.value = false
            }
        }
    }

}