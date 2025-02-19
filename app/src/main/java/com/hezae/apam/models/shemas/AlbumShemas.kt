package com.hezae.apam.models.shemas

import com.google.gson.annotations.SerializedName


class CreateAlbum (
    var name: String,
    var description: String,
    @SerializedName("created_at")
    var createdAt: String
)

class Album (
    var id: String,
    var user_id: String,
    var name: String,
    var description: String,
    var tag: String,
    var created_at: String,
    var cover_picture_id: String,
    var public: Boolean,
    var count: Int
)
