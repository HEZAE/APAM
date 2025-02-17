package com.hezae.apam.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hezae.apam.datas.ApiResult
import com.hezae.apam.models.shemas.CreateAlbum
import com.hezae.apam.tools.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Response

class AlbumViewModel : ViewModel() {
    val api = RetrofitInstance.albumApi

    //创建相册
    suspend fun createPicture(
        token: String,
        album: CreateAlbum,
        onFinished: (ApiResult<String>) -> Unit
    ) {
        parseResponse(api.createAlbum("bearer $token", album), onFinished)
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
                    val errorBody  =    response.errorBody()
                    ApiResult(false, response.code(),errorBody.string() )
                }
            } catch (e: Exception) {
                ApiResult(false, 500, e.message.toString())
            }
            if (!apiResult.success) {
                Log.e("PictureViewModel", "error:"+apiResult.msg)
            }

            onFinished(apiResult)
        }
    }
}