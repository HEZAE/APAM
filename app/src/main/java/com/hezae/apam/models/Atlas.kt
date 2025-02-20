package com.hezae.apam.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import java.io.File

// 图集模型
open class Atlas(
    var id: String = "",
    var title: String = "测试相册",
    var size: Int = 0,
    var isPrivate: Boolean = false, // 是否隐私
    var coverId: String = "", // 封面 ID
    var discription: String = "",
)

class AtlasItem(
    id: String = "",
    title: String = "测试相册",
    size: Int = 0,
    isPrivate: Boolean = false,
    coverId: String = "",
    discription: String = "",
    isSelected:Boolean = false,
    isInit: Boolean = false,
    isLoading: Boolean = false, // 是否正在加载
    isError: Boolean = false // 是否加载失败
) : Atlas(id, title, size, isPrivate, coverId) {
    var isInit: MutableState<Boolean> = mutableStateOf(isInit)
    var isLoading: MutableState<Boolean> = mutableStateOf(isLoading)
    var isError: MutableState<Boolean> = mutableStateOf(isError)
    var isSelected: MutableState<Boolean> = mutableStateOf(isSelected)
}