package com.hezae.apam.datas

/**
 * 服务端返回的数据格式
 *
 * @param success 是否成功
 * @param code 状态代码
 * @param msg 消息
 * @param data 返回数据
 */
data class ApiResult<T>(
    val success: Boolean, // 是否成功
    val code: Int,        // 状态代码
    val msg: String,      // 消息
    val data: T? = null   // 返回数据，默认为 null
)

