package com.hezae.apam.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hezae.apam.datas.ApiResult
import com.hezae.apam.models.shemas.Album
import com.hezae.apam.models.shemas.CreateAlbum
import com.hezae.apam.tools.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Response

class AlbumViewModel : ViewModel() {
    private val api = RetrofitInstance.albumApi

    //创建相册
    fun createPicture(
        token: String,
        album: CreateAlbum,
        onFinished: (ApiResult<String>) -> Unit
    ) {
        viewModelScope.launch {
            parseResponse(api.createAlbum("bearer $token", album), onFinished)
        }
    }
    
    //查找所有相册
    fun getAlbums(
        token: String,
        onFinished: (ApiResult<List<Album>>) -> Unit
    ) {

        viewModelScope.launch {
            parseResponse( api.getAlbums("bearer $token"), onFinished)
        }

    }

    //解析请求
    private fun <T> parseResponse(
        response: Response<ApiResult<T>>,
        onFinished: (ApiResult<T>) -> Unit
    ) {
        var apiResult:ApiResult<T>
        viewModelScope.launch {
            apiResult = try {
                if (response.isSuccessful) {
                    response.body()!!
                }else {
                    val errorBody  =  response.errorBody()
                    ApiResult(false, response.code(),errorBody!!.bytes().toString() )
                }
            } catch (e: Exception) {
                ApiResult(false, 500, e.message.toString())
            }
            if (!apiResult.success) {
                Log.e("AlbumViewModel", "error:"+apiResult.msg)
            }

            onFinished(apiResult)
        }
    }
}