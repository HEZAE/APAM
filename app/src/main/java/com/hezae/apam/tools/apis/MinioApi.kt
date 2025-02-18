package com.hezae.apam.tools.apis

import com.hezae.apam.datas.ApiResult
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface MinioApi
{
    //获取预上传链接
    @GET("minio/presigned_url/upload")
    suspend fun getUploadUrl(
        @Header("Authorization") token: String,
        @Query("album_id") albumId: String,
        @Query("picture_id") pictureId: String
    ): Response<ApiResult<String>>

    //获取下载链接
    @GET("minio/presigned_url/download")
    suspend fun getDownloadUrl(
        @Header("Authorization") token: String,
        @Query("album_id") albumId: String,
        @Query("picture_id") pictureId: String
    ): Response<ApiResult<String>>

    //上传文件
    @Multipart
    @PUT
    suspend fun uploadFile(
        @Url url:String,
        @Part file: MultipartBody.Part,
    ): Response<Unit>
}