package com.lishuaihua.net.httputils

import android.content.Context
import com.lishuaihua.net.util.checkNetwork
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import kotlin.jvm.Throws

class CacheInterceptor(var context: Context) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (context.checkNetwork() == true) {
            val response = chain.proceed(request)
            // read from cache for 0 s  有网络不会使用缓存数据
            val maxAge = 0
            return response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control", "public, max-age=$maxAge")
                .build()
        } else {
            //无网络时强制使用缓存数据
            request = request.newBuilder()
                .cacheControl(CacheControl.FORCE_CACHE)
                .build()
            val response = chain.proceed(request)
            //缓存时间3天
            val maxStale = 60 * 60 * 24 * 3
            return response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header(
                    "Cache-Control",
                    "public, only-if-cached, max-stale=$maxStale"
                )
                .build()
        }
    }
}
