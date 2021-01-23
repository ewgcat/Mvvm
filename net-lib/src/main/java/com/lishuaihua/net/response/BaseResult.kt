package com.lishuaihua.net.response

open class BaseResult<T> {
    val msg: String? = null
    val code: Int = 0
    val data: T? = null
}