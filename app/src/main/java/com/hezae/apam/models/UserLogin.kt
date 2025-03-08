package com.hezae.apam.models

class UserLogin(
    var username: String = "",
    var password: String = ""
)

class UserRegister(
    var username: String = "",
    var password: String = "",
    var nickname: String = "",
    var email: String = "",
    var code: String = ""
)