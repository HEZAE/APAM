package com.hezae.apam.tools.apis

import com.hezae.apam.datas.ApiResult
import com.hezae.apam.models.shemas.Album
import com.hezae.apam.models.shemas.CreateAlbum
import com.hezae.apam.models.shemas.Picture
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

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

    //获取相册封面
    @GET("album/cover")
    suspend fun getAlbumCover(
        @Header("Authorization") token: String,
        @Query("album_id") albumId: String
    ): Response<ApiResult<Picture>>

    //删除相册
    @DELETE("album")
    suspend fun deleteAlbum(
        @Header("Authorization") token: String,
        @Query("album_id") albumId: String
    ): Response<ApiResult<String>>
}