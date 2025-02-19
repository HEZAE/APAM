package com.hezae.apam.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hezae.apam.datas.ApiResult
import com.hezae.apam.datas.PresignedURL
import com.hezae.apam.models.shemas.Album
import com.hezae.apam.models.shemas.DeletePicture
import com.hezae.apam.models.shemas.Picture
import com.hezae.apam.tools.RetrofitInstance
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File

class PictureViewModel : ViewModel() {
    //当前选择的相册
    var album = mutableStateOf(
        Album(
            id = "",
            user_id = "",
            name = "测试相册",
            description = "",
            tag = "",
            created_at = "",
            cover_picture_id = "",
            public = false,
            count = 0
        )
    )

    val pictureApi = RetrofitInstance.pictureApi
    val minioApi = RetrofitInstance.minioApi

    //根据图片id获取图片
    fun getPicture(
        token: String,
        pictureId: String,
        onFinished: (ApiResult<Picture>) -> Unit
    ) {
        try {
            viewModelScope.launch {
                parseResponse(pictureApi.getPicture("bearer $token", pictureId), onFinished)
            }
        } catch (e: Exception) {
            Log.e("PictureViewModel", "error:${e.message}")
        }
    }

    //根据图片集合删除图片
    fun deletePictures(
        token: String,
        pictureIds: List<String>,
        onFinished: (ApiResult<String>) -> Unit
    ) {
        //都是一样的相册
        val albumIds:List<String> = pictureIds.map {album.value.id }
            val body = DeletePicture(albumIds, pictureIds)
            try {
            viewModelScope.launch {
                parseResponse(minioApi.deleteFiles("bearer $token",body ),
                    onFinished)
            }
        } catch (e: Exception) {
            Log.e("PictureViewModel", "error:${e.message}")
        }
    }


    //获取图片列表
    fun getPicturesByAlbum(
        token: String,
        onFinished: (ApiResult<List<Picture>>) -> Unit
    ) {
        try {
            viewModelScope.launch {
                parseResponse(pictureApi.getPicturesByAlbum("bearer $token", album.value.id), onFinished)
            }
        } catch (e: Exception) {
            Log.e("PictureViewModel", "error:${e.message}")
        }
    }

    //获取预签名上传地址
    fun getPresignedUrl( token: String,pictureId: String,name: String,
        width:Float,height:Float,tags: Map<String, String>,level: Int,
        onFinished: (ApiResult<String>) -> Unit)
    {
        try {
            viewModelScope.launch {
                if (album.value.id.isEmpty()) {
                    onFinished(ApiResult(false, 500, "请选择相册"))
                    return@launch
                }
                parseResponse(
                    minioApi.getUploadUrl(
                        "bearer $token",
                        PresignedURL(album.value.id, pictureId, name,width,height,level, tags)
                    ), onFinished
                )
            }
        } catch (e: Exception) {
            Log.e("PictureViewModel", "error:${e.message}")
            onFinished(ApiResult(false, 500, e.message.toString()))
        }
    }

    //获取预签名下载地址
    fun getPresignedDownloadUrl( token: String,pictureId:String,
        onFinished: (ApiResult<String>) -> Unit) {
        try {
            viewModelScope.launch {
                if (album.value.id.isEmpty()) {
                    onFinished(ApiResult(false, 500, "请选择相册"))
                    return@launch
                }
                parseResponse(
                    minioApi.getDownloadUrl(token,albumId = album.value.id, pictureId = pictureId,),
                    onFinished
                )
            }
        }catch (e: Exception){
            Log.e("PictureViewModel", "error:${e.message}")
            onFinished(ApiResult(false, 500, e.message.toString()))
        }
    }

    //上传文件
    fun uploadFile(
        presignedURL: String,
        file: File,
        onFinished: (ApiResult<String>) -> Unit
    ) {
        viewModelScope.launch {
            try {

                val requestBody = file.asRequestBody("image/jpg".toMediaTypeOrNull())
                val response = minioApi.uploadFile(presignedURL, requestBody)
                if (response.isSuccessful) {
                    Log.e("上传文件", "上传成功")
                    onFinished(ApiResult(true, 200, "上传成功"))
                } else {
                    val errorBody = response.errorBody()
                    val errorContent = errorBody?.string()
                    Log.e("上传文件", "上传失败$errorContent")
                    onFinished(ApiResult(false, 200, "上传失败"))
                }
            } catch (e: Exception) {
                onFinished(ApiResult(false, 500, e.message.toString()))
                Log.e("上传文件", "上传失败${e.message}")
            }
        }
    }

    //解析请求
    private fun <T> parseResponse(
        response: Response<ApiResult<T>>,
        onFinished: (ApiResult<T>) -> Unit
    ) {
        var apiResult: ApiResult<T>
        viewModelScope.launch {
            apiResult = try {
                if (response.isSuccessful) {
                    response.body()!!
                } else {
                    val errorBody = response.errorBody()
                    ApiResult(false, response.code(), errorBody!!.string())
                }
            } catch (e: Exception) {
                ApiResult(false, 500, e.message.toString())
            }
            if (!apiResult.success) {
                Log.e("PictureViewModel", "error:" + apiResult.msg)
            }
            onFinished(apiResult)
        }
    }
}