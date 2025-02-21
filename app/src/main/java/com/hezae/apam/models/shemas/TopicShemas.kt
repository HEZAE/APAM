package com.hezae.apam.models.shemas

import java.util.*

data class Topic(
    val id: String,              // 主键UUID
    val title: String,           // 标题
    val content: String,         // 内容
    val createdAt: String,       // 创建时间
    val updatedAt: String,       // 更新时间
    val userId: String,            // 用户ID
    val status: String = "normal", // 状态
    val pictures: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val likes: Int = 0,          // 点赞数
    val collects: Int = 0,       // 收藏数
    val comments: Int = 0        // 评论数
)
