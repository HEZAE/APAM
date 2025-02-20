package com.hezae.apam.models.shemas

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.google.gson.annotations.SerializedName

data class CreatePicture(
    val id: String,
    val name: String,
    val description: String,
    @SerializedName("created_at")
    val createdAt: String,  // 对应 Python 的 datetime
    val width: Float,              // 对应 Python 的 float
    val height: Float,             // 对应 Python 的 float
    val level: Int,                // 对应 Python 的 int
    @SerializedName("album_id")
    val albumId: String            // 对应 Python 的 str
)

data class DeletePicture(
    @SerializedName("album_ids")
    val albumIds: List<String>,
    @SerializedName("picture_ids")
    val pictureIds: List<String>
)


open class Picture(
    val id: String,
    @SerializedName("user_id")
    val userId: String,        // 归属用户
    @SerializedName("album_id")
    val albumId: String,       // 归属相册
    var name: String,        // 图片名称
    var description: String, // 图片描述（可空）
    @SerializedName("created_at")
    val createdAt: String, // 上传时间
    val width: Float,        // 图片宽度
    val height: Float,       // 图片高度
    var level: Int = 1       // 权限等级（默认值1）
)

class PictureItem(
    id: String,            // 对应 UUIDField（主键）
    userId: String,        // 归属用户
    albumId: String,       // 归属相册
    name: String,        // 图片名称
    description: String, // 图片描述（可空）
    createdAt: String, // 上传时间
    width: Float,        // 图片宽度
    height: Float,       // 图片高度
    level: Int = 1,      // 权限等级（默认值1）
    isInit: Boolean = false,
    isLoading: Boolean = false, // 是否正在加载
    isError: Boolean = false, // 是否加载失败
    isSelected: Boolean = false,

) : Picture(
    id = id,
    userId = userId,
    albumId = albumId,
    name = name,
    description = description,
    createdAt = createdAt,
    width = width,
    height = height,
    level = level
) {
    var isInit: MutableState<Boolean> = mutableStateOf(isInit)
    var isLoading: MutableState<Boolean> = mutableStateOf(isLoading)
    var isError: MutableState<Boolean> = mutableStateOf(isError)
    var isSelected: MutableState<Boolean> = mutableStateOf(isSelected)
}
