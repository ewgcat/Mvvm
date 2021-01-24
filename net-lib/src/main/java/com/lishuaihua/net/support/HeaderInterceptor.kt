package com.lishuaihua.net.support

import java.io.IOException

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * Retrofit2 Header拦截器。用于保存和设置Cookies
 */
class HeaderInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val url = original.url.toString()
        //构建新的请求体信息
        val builder = original.newBuilder().url(url)
        tryAddHeaderUserToken(builder)
        val newRequest = builder.build()
        return chain.proceed(newRequest)
    }

    //头部添加token
    private fun tryAddHeaderUserToken(builder: Request.Builder) {

    }

}
