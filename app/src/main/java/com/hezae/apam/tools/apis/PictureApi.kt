package com.hezae.apam.tools.apis

import com.hezae.apam.datas.ApiResult
import com.hezae.apam.models.shemas.CreatePicture
import com.hezae.apam.models.shemas.Picture
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PictureApi {
    //创建相片
//    @POST("picture/create")
//    suspend fun createPicture(
//        @Header("Authorization") token: String,
//        @Body picture: CreatePicture
//    ): Response<ApiResult<String>>

    //根据相片ID获取相片信息
    @GET("picture/{picture_id}")
    suspend fun getPicture(
        @Header("Authorization") token: String,
        @Path("picture_id") pictureId: String
    ): Response<ApiResult<Picture>>

    //删除相片
    @DELETE("picture/{picture_id}")
    suspend fun deletePicture(
        @Header("Authorization") token: String,
        @Path("picture_id") pictureId: String
    ): Response<ApiResult<String>>

    //根据相册ID获取相片列表
    @GET("picture/album/{album_id}")
    suspend fun getPicturesByAlbum(
        @Header("Authorization") token: String,
        @Path("album_id") albumId: String
    ): Response<ApiResult<List<Picture>>>

    //更新相片名字
    @PUT
    suspend fun updatePictureName(
        @Header("Authorization") token: String,
        @Path("picture_id") pictureId: String,
        @Query("name") name: String,
        @Query("data") data: String,
    ): Response<ApiResult<String>>

    //移动相片到其他相册
    @PUT
    suspend fun movePictureToAlbum(
        @Header("Authorization") token: String,
        @Path("picture_id") pictureId: String,
        @Query("old_album_id") albumId: String,
        @Query("new_album_id") newAlbumId: String,
    ): Response<ApiResult<String>>
}