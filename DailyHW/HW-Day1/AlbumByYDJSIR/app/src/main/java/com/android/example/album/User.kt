package com.android.example.album

/**
 * 用户的数据类
 *
 * @author YDJSIR
 */
data class User(
    val name: String,
    val email: String,
    var password: String,
    val age: Int,
    val gender: String,
    val address: String
)