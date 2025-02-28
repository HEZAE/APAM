package com.hezae.apam.models.shemas

import android.util.Log
import com.google.gson.annotations.SerializedName
import com.google.gson.*
import com.google.gson.annotations.JsonAdapter
import java.lang.reflect.Type
data class Topic(
    val id: String,              // 主键UUID
    val title: String,           // 标题
    val content: String,         // 内容
    @SerializedName("created_at")
    val createdAt: String,       // 创建时间
    @SerializedName("updated_at")
    val updatedAt: String,       // 更新时间
    @SerializedName("user_id")
    val userId: String,            // 用户ID
    val status: String = "normal", // 状态
    @JsonAdapter(PicturesTypeAdapter::class)
    val pictures: List<String> = emptyList(),
    @JsonAdapter(TagsTypeAdapter::class)  // 使用自定义的 TypeAdapter
    val tags: List<Int> = emptyList(),
    val likes: Int = 0,          // 点赞数
    val collects: Int = 0,       // 收藏数
    val comments: Int = 0        // 评论数
)




// 自定义 TypeAdapter 来处理 tags 字段的序列化和反序列化
class TagsTypeAdapter : JsonSerializer<List<Int>>, JsonDeserializer<List<Int>> {
    override fun serialize(src: List<Int>, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        // 将 List<Int> 序列化成 JSON 字符串
        return JsonPrimitive(src.toString())  // 转换为 JSON 字符串格式
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): List<Int> {
        // 将 JSON 字符串反序列化为 List<Int>
        return if (json.isJsonArray) {
            json.asJsonArray.map { it.asInt }
        } else {
            // 如果是字符串 "[]"
            json.asString.removeSurrounding("[", "]")
                .split(",")
                .map { it.trim().toIntOrNull() ?: 0 }
        }
    }
}

// 自定义 TypeAdapter 来处理 pictures 字段的序列化和反序列化
class PicturesTypeAdapter : JsonSerializer<List<String>>, JsonDeserializer<List<String>> {
    override fun serialize(src: List<String>, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.toString())
    }
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): List<String> {
        Log.d("PicturesTypeAdapter", "deserialize: $json")
        val results = json.asString
        return if (results.equals("[]" )){ emptyList()
        }else{
            results.removeSurrounding("[", "]") // 移除两端的方括号
                .split(",") // 按逗号分割
                .map { it.trim().removeSurrounding("\"") }  // 移除每个元素的双引号
        }
    }
}

class CreatedTopicModel(
    val title: String,// 标题
    val content: String,// 内容
    val tags: List<Int> = emptyList(),// 标签
    val pictures: List<String> = emptyList(),// 图片
    @SerializedName("created_at")
    val createdAt:String
    )