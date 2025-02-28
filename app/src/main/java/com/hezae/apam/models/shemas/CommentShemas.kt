package com.hezae.apam.models.shemas

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.gson.annotations.SerializedName

// 评论
data class Comment(
    val id: String,
    val content: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    val status: Int,
    @SerializedName("topic_id")
    val topicId: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("parent_comment_id")
    val parentCommentId: String,
    @SerializedName("prefix_comment_id")
    val prefixCommentId: String,
    @SerializedName("suffix_comment_id")
    val suffixCommentId: String,
)

//创建评论
data class CreateComment(
    val content: String,//内容
    @SerializedName("topic_id")
    val topicId: String,//话题id
    @SerializedName("parent_comment_id")
    val parentCommentId: String,//父评论id
    @SerializedName("prefix_comment_id")
    val prefixCommentId: String,
    @SerializedName("suffix_comment_id")
    val suffixCommentId: String,
    @SerializedName("created_at")
    val createdAt: String,
)

class CommentItem(
    val id: String,
    val content: String,
    val createdAt: String,
    val updatedAt: String,
    val status: Int,
    val topicId: String,
    val userId: String,
    val parentCommentId: String,
    val prefixCommentId: String,
    val suffixCommentId: String,
    val subComments: SnapshotStateList<CommentItem>
)
