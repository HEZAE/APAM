package com.hezae.apam.tools

import com.hezae.apam.datas.Post
import retrofit2.http.GET

interface ApiService {
    @GET("posts")
    suspend fun getPosts(): List<Post>
}
