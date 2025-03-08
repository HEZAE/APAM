package com.hezae.apam.tools

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hezae.apam.tools.apis.AlbumApi
import com.hezae.apam.tools.apis.CommentApi
import com.hezae.apam.tools.apis.MinioApi
import com.hezae.apam.tools.apis.PictureApi
import com.hezae.apam.tools.apis.TopicApi
import com.hezae.apam.tools.apis.UserApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val gson: Gson = GsonBuilder()
        .setLenient() // 设置宽松模式
        .create()
    private const val BASE_URL = "http://192.168.20.27:8000"
    val userApi: UserApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(UserApi::class.java)
    }

    val pictureApi: PictureApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(PictureApi::class.java)
    }

    val albumApi: AlbumApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(AlbumApi::class.java)
    }

    val minioApi: MinioApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(MinioApi::class.java)
    }
    val topicApi:TopicApi by  lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(TopicApi::class.java)
    }

    val commentApi:CommentApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(CommentApi::class.java)
    }
}