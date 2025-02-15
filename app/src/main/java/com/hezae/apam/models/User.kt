package com.hezae.apam.models

class User(
    val id: String,
    val username: String,
    val nickname: String,
    val email: String,
    val level: Int,
    val capacity: Float,
    val capacity_used: Float,
    val count: Int,
    val count_used: Int,
    val status: String
)