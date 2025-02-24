package com.hezae.apam.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hezae.apam.datas.ApiResult
import com.hezae.apam.models.shemas.CreatedTopicModel
import com.hezae.apam.tools.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Response
import java.net.SocketTimeoutException

class TopicViewModel: ViewModel()
{
    private val topicApi= RetrofitInstance.topicApi
    fun createTopic(
        token:String,
        createdTopicModel: CreatedTopicModel,
        onFinished: (ApiResult<String>) -> Unit
    ){
        viewModelScope.launch {
            parseResponse({topicApi.createdTopic("bearer $token",createdTopicModel)}, onFinished)
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
                if (response.isSuccessful&&response.body()!=null) {
                    apiResult = if (response.body()!!.success){
                        ApiResult(true, response.code(),"", response.body()!!.data)
                    }else{
                        ApiResult(false, response.code(),response.body()!!.msg)
                    }
                }else {
                    val errorBody  = response.errorBody()
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
                Log.e("PictureViewModel", "error:"+apiResult.msg)
            }
            onFinished(apiResult)
        }
    }
}