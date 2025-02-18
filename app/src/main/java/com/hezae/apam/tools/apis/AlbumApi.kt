package com.hezae.apam.tools.apis

import com.hezae.apam.datas.ApiResult
import com.hezae.apam.models.shemas.Album
import com.hezae.apam.models.shemas.CreateAlbum
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AlbumApi {
    @POST("album/")
    suspend fun createAlbum(
        @Header("Authorization") token: String,
        @Body album: CreateAlbum
    ): Response<ApiResult<String>>

    //获取这个用户的所有相册
    @GET("album/list")
    suspend fun getAlbums(
        @Header("Authorization") token: String
    ): Response<ApiResult<List<Album>>>
}