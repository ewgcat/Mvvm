package com.lishuaihua.net.error

import retrofit2.HttpException

object ErrorUtil {

    fun getError(e: Throwable): ErrorResult {
        val errorResult = ErrorResult()
        if (e is HttpException) {
            errorResult.code = e.code()
        }
        errorResult.msg = e.message
        if (errorResult.msg.isNullOrEmpty()) errorResult.msg = "网络请求失败，请重试"
        return errorResult
    }

}