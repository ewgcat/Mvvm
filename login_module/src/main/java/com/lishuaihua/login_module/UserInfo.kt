package com.lishuaihua.login_module

data class UserInfo(
    val actionType: Int,
    val birthday: String,
    val email: String,
    val hasPassword: Int,
    val id: Int,
    val loginId: String,
    val nickname: String,
    val phone: String,
    val platform: Int,
    val recommendCode: String,
    val sex: Int,
    val storeId: Int,
    val token: String,
    val userHeadPic: String,
    val userLevel: Int,
    val username: String
)