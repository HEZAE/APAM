package com.hezae.apam.datas

import com.google.gson.annotations.SerializedName

data class PresignedURL
(
    @SerializedName("album_id")
    val albumId: String,
    @SerializedName("picture_id")
    val pictureId: String,
    val name: String,
    val width: Float,
    val height: Float,
    val size :Long,
    val level: Int,
    val tags: String,
)