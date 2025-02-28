package com.hezae.apam.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hezae.apam.datas.ApiResult
import com.hezae.apam.models.shemas.Comment
import com.hezae.apam.models.shemas.CreateComment
import com.hezae.apam.models.shemas.CreatedTopicModel
import com.hezae.apam.models.shemas.Topic
import com.hezae.apam.tools.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Response
import java.net.SocketTimeoutException

class TopicViewModel: ViewModel()
{
    private val topicApi= RetrofitInstance.topicApi
    private val commentApi  = RetrofitInstance.commentApi
    val typeOptions = listOf(
        "风景",
        "人像",
        "街拍",
        "婚礼",
        "建筑",
        "运动",
        "静物",
        "旅行",
        "纪录片",
        "艺术",
        "夜景",
        "蓝天",
        "抽象",
        "其他"
    )    //内容
    //键值对，用于缓存得到的用户名
    val userNameCache = mutableMapOf<String,String>()
    //选中的话题
    var selectedTopic by  mutableStateOf<Topic?>(null)

    //创建帖子
    fun createTopic(
        token:String,
        createdTopicModel: CreatedTopicModel,
        onFinished: (ApiResult<String>) -> Unit
    ){
        viewModelScope.launch {
            parseResponse({topicApi.createdTopic("bearer $token",createdTopicModel)}, onFinished)
        }
    }

    //获取用户名
    fun getUserName(
        token:String,
        userId:String,
        onFinished: (ApiResult<String>) -> Unit
    ){
        viewModelScope.launch {
            parseResponse({topicApi.getUserName("bearer $token",userId)}, onFinished)
        }
    }

    //随机获取帖子
    fun getRandomTopic(
        token:String,
        onFinished: (ApiResult<List<Topic>>) -> Unit
    ){
        viewModelScope.launch {
            parseResponse({topicApi.getRandomTopic("bearer $token")}, onFinished)
        }
    }

    //指定类型获取帖子
    fun getTypeTopic(
        token:String,
        type:Int,
        onFinished: (ApiResult<List<Topic>>) -> Unit
    ){
        viewModelScope.launch {
            parseResponse({topicApi.getTypeTopic("bearer $token",type)}, onFinished)
        }
    }


    //创建评论
    fun createComment(
        token:String,
        createComment: CreateComment,
        onFinished: (ApiResult<String>) -> Unit
    ){
        viewModelScope.launch {
            parseResponse({commentApi.createComment("bearer $token",createComment)}, onFinished)
        }
    }

    //根据话题id获取评论
    fun getTopComments(
        token:String,
        topicId:String,
        onFinished: (ApiResult<List<Comment>>) -> Unit
    ){
        viewModelScope.launch {
            parseResponse({commentApi.getTopComments("bearer $token",topicId)}, onFinished)
        }
    }

    //根据评论id获取评论
    fun getComment(
        token:String,
        commentId:String,
        onFinished: (ApiResult<Comment>) -> Unit
    ){
        viewModelScope.launch {
            parseResponse({commentApi.getComment("bearer $token",commentId)}, onFinished)
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
                Log.e("TopicViewModel", "error:"+apiResult.msg)
            }
            onFinished(apiResult)
        }
    }
}