package com.hezae.apam.tools

import com.hezae.apam.datas.ApiResult
import com.hezae.apam.models.User
import com.hezae.apam.models.UserLogin
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    //获取Token
    @POST("user/login")
    suspend fun login(
        @Body user: UserLogin
    ): Response<ApiResult<String>>

    //获取用户信息
    @GET("user/info")
    suspend fun getUserInfo(@Header("Authorization") token: String, ): Response<ApiResult<User>>
}
