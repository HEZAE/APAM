package com.hezae.apam.models.shemas

import com.google.gson.annotations.SerializedName

//请求模型·
data class RequestModel(
    @SerializedName("model_sn")
    val modelSn: Int,//模型编号
    @SerializedName("user_id")
    val token : String,
    //其他信息
    val other: String = ""
)