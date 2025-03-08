package com.hezae.apam.models.shemas

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.gson.annotations.SerializedName

// 评论
data class Comment(
    val id:Int,
    val content: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    val status: String,
    @SerializedName("topic_id")
    val topicId: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("parent_comment_id")
    val parentCommentId: Int,
    @SerializedName("child_comments_count")
    val childCommentsCount: Int,
)

//创建评论
data class CreateComment(
    val content: String,//内容
    @SerializedName("topic_id")
    val topicId: String,//话题id
    @SerializedName("parent_comment_id")
    val parentCommentId: Int,//父评论id
    @SerializedName("created_at")
    val createdAt: String,
)

class CommentItem(
    val id: Int,
    val content: String,
    val createdAt: String,
    val updatedAt: String,
    val status: String,
    val topicId: String,
    val userId: String,
    val parentCommentId: Int,
    val childCommentsCount: Int,
    val subComments: SnapshotStateList<CommentItem>
)
