package com.hezae.apam.tools.apis

import com.hezae.apam.datas.ApiResult
import com.hezae.apam.models.User
import com.hezae.apam.models.UserLogin
import com.hezae.apam.models.UserRegister
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApi {

    //获取Token
    @POST("user/login")
    suspend fun login(
        @Body user: UserLogin
    ): Response<ApiResult<String>>

    //获取用户信息
    @GET("user/info")
    suspend fun getUserInfo(@Header("Authorization") token: String, ): Response<ApiResult<User>>

    //获取验证码
    @GET("user/get_code")
    suspend fun getCode(
        @Query("email") email: String,
        @Query("username") username: String
    ): Response<ApiResult<String>>

    //注册
    @POST("user/register")
    suspend fun register(
        @Body user: UserRegister
    ): Response<ApiResult<String>>
}
