package com.hezae.apam.models.shemas

import com.google.gson.annotations.SerializedName


class CreateAlbum (
    var name: String,
    var description: String,
    var public: Boolean,
    @SerializedName("created_at")
    var createdAt: String

)

class Album (
    var id: String,
    @SerializedName("user_id")
    var userId: String,
    var name: String,
    var description: String,
    var tag: String,
    @SerializedName("created_at")
    var createdAt: String,
    @SerializedName("cover_picture_id")
    var coverPictureId: String,
    var public: Boolean,
    var count: Int
)
