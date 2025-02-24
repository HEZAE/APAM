package com.hezae.apam.tools.apis

import com.hezae.apam.datas.ApiResult
import com.hezae.apam.models.shemas.CreatePicture
import com.hezae.apam.models.shemas.CreatedTopicModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface TopicApi {
    @POST("forum/topic/create")
    suspend fun createdTopic(
        @Header("Authorization") token: String,
        @Body topic: CreatedTopicModel
    ): Response<ApiResult<String>>
}