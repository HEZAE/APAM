package com.hezae.apam.models.shemas
import java.util.UUID
import java.time.LocalDateTime

data class CreatePicture(
    val name: String,
    val description: String,
    val updatedAt: LocalDateTime,  // 对应 Python 的 datetime
    val width: Float,              // 对应 Python 的 float
    val height: Float,             // 对应 Python 的 float
    val level: Int,                // 对应 Python 的 int
    val albumId: String            // 对应 Python 的 str
)


data class Picture(
    val id: UUID,            // 对应 UUIDField（主键）
    val userId: UUID,        // 归属用户
    val albumId: UUID,       // 归属相册
    val name: String,        // 图片名称
    val description: String?, // 图片描述（可空）
    val createdAt: LocalDateTime, // 上传时间
    val width: Float,        // 图片宽度
    val height: Float,       // 图片高度
    val level: Int = 1       // 权限等级（默认值1）
)
