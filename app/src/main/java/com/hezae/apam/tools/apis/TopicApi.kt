package com.hezae.apam.tools.apis

import com.hezae.apam.datas.ApiResult
import com.hezae.apam.models.shemas.CreatePicture
import com.hezae.apam.models.shemas.CreatedTopicModel
import com.hezae.apam.models.shemas.Topic
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface TopicApi {
    @POST("forum/topic/create")
    suspend fun createdTopic(
        @Header("Authorization") token: String,
        @Body topic: CreatedTopicModel
    ): Response<ApiResult<String>>

    //随机获取帖子
    @GET("forum/topics/random")
    suspend fun getRandomTopic(
        @Header("Authorization") token: String,
    ): Response<ApiResult<List<Topic>>>

    //随机获取帖子
    @GET("forum/topics/type")
    suspend fun getTypeTopic(
        @Header("Authorization") token: String,
        @Query("type") type: Int,
    ): Response<ApiResult<List<Topic>>>

    //根据发布帖子的用户id获取用户名
    @GET("forum/username/topic")
    suspend fun getUserName(
        @Header("Authorization") token: String,
        @Query("user_id") userId: String,
    ): Response<ApiResult<String>>
}