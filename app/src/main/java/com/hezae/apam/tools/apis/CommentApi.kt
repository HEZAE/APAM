package com.hezae.apam.tools.apis

import com.hezae.apam.datas.ApiResult
import com.hezae.apam.models.shemas.Comment
import com.hezae.apam.models.shemas.CreateComment
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface CommentApi
{
    //创建评论
    @POST("forum/comment/create")
    suspend fun createComment(
        @Header("Authorization") token: String,
        @Body comment: CreateComment
    ): Response<ApiResult<String>>

    //根据话题ID获取所有评论
    @GET("forum/comment/top")
    suspend fun getTopComments(
        @Header("Authorization") token: String,
        @Query("topic_id") topicId: String
    ): Response<ApiResult<List<Comment>>>

    //根据评论id获取子评论
    @GET("forum/comment/child")
    suspend fun getChildComments(
        @Header("Authorization") token: String,
        @Query("comment_id") commentId: String
    ): Response<ApiResult<List<Comment>>>


    //根据评论ID获取评论
    @GET("forum/comment/get")
    suspend fun getComment(
        @Header("Authorization") token: String,
        @Query("comment_id") commentId: String
    ): Response<ApiResult<Comment>>

    //根据评论ID删除评论
    @DELETE("forum/comment/delete")
    suspend fun deleteComment(
        @Header("Authorization") token: String,
        @Query("comment_id") commentId: String
    ): Response<ApiResult<String>>

    //根据评论ID更新评论
    @POST("forum/comment/update")
    suspend fun updateComment(
        @Header("Authorization") token: String,
        @Body comment: Comment
    ): Response<ApiResult<String>>

}