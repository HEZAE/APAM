package com.hezae.apam.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hezae.apam.datas.ApiResult
import com.hezae.apam.models.shemas.Album
import com.hezae.apam.models.shemas.CreateAlbum
import com.hezae.apam.models.shemas.Picture
import com.hezae.apam.tools.RetrofitInstance
import com.hezae.apam.tools.RetrofitInstance.minioApi
import kotlinx.coroutines.launch
import retrofit2.Response
import java.net.SocketTimeoutException

class AlbumViewModel : ViewModel() {
    private val api = RetrofitInstance.albumApi

    //创建相册
    fun createPicture(
        token: String,
        album: CreateAlbum,
        onFinished: (ApiResult<String>) -> Unit
    ) {
        viewModelScope.launch {
            parseResponse({api.createAlbum("bearer $token", album)}, onFinished)
        }
    }
    
    //查找所有相册
    fun getAlbums(
        token: String,
        onFinished: (ApiResult<List<Album>>) -> Unit
    ) {
        viewModelScope.launch {
            parseResponse({api.getAlbums("bearer $token")} , onFinished)
        }
    }

    //获取相册封面
    fun getAlbumCover(
        token: String,
        albumId: String,
        onFinished: (ApiResult<Picture>) -> Unit
    ) {
        viewModelScope.launch {
            parseResponse({api.getAlbumCover("bearer $token", albumId)}, onFinished)
        }
    }

    //删除相册
    fun deleteAlbum(
        token: String,
        albumId: String,
        onFinished: (ApiResult<String>) -> Unit
    ) {
        viewModelScope.launch {
            parseResponse({api.deleteAlbum("bearer $token", albumId)}, onFinished)
        }
    }


    //获取预签名下载地址
    fun getPresignedDownloadUrl(token: String, albumId:String, pictureId:String,
                                onFinished: (ApiResult<String>) -> Unit) {
        try {
            viewModelScope.launch {
                if (pictureId.isEmpty()) {
                    onFinished(ApiResult(false, 500, "请选择相册"))
                    return@launch
                }
                parseResponse(
                    {minioApi.getDownloadUrl(token,albumId =albumId, pictureId = pictureId,)},
                    onFinished
                )
            }
        }catch (e: Exception){
            Log.e("AlbumViewModel", "error:${e.message}")
            onFinished(ApiResult(false, 500, e.message.toString()))
        }
    }

    //解析请求
    private fun <T> parseResponse(
        request: suspend()-> Response<ApiResult<T>>,
        onFinished: (ApiResult<T>) -> Unit
    ) {
        var apiResult:ApiResult<T>
        viewModelScope.launch {
             try {
                 val response = request()
                 if (response.isSuccessful) {
                    apiResult = response.body()!!
                }else {
                    val errorBody  =  response.errorBody()
                    apiResult = if (errorBody == null){
                        ApiResult(false, response.code(),"")
                    }else{
                        ApiResult(false, response.code(), errorBody.string())
                    }
                }
            }
             catch (e: SocketTimeoutException){
                 apiResult = ApiResult(false, 500, "网络请求超时")
             }
             catch (e: Exception) {
              apiResult =  ApiResult(false, 500, e.message.toString())
            }
            if (!apiResult.success) {
                Log.e("AlbumViewModel", "code:"+apiResult.code+"error:"+apiResult.msg)
            }
            onFinished(apiResult)
        }
    }
}