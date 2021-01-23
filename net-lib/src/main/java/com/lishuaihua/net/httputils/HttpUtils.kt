package com.lishuaihua.net.httputils

import android.content.Context
import android.util.Log
import com.lishuaihua.net.httpsfactroy.HTTPSCerUtils
import com.lishuaihua.net.support.CookieJarImpl
import com.lishuaihua.net.support.HeaderInterceptor
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URLConnection
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier


class HttpUtils {

    companion object {
        var mRetrofit: Retrofit? = null

        //初始化一般请求客户端
        fun init(context: Context, baseUrl: String, rawId: Int, builder: OkHttpClient.Builder, isDebug: Boolean): Retrofit? {
            if (mRetrofit == null) {
                if (isDebug) {
                    // Log信息拦截器
                    val loggingInterceptor = HttpLoggingInterceptor { message -> Log.d("http", message) }
                    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                    builder.addInterceptor(loggingInterceptor)
                }
                if (rawId != 0) {
                    HTTPSCerUtils.setCertificate(context, builder, rawId)
                    builder.hostnameVerifier(HostnameVerifier { hostname, session -> true })
                }
                builder.connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true) // 失败重发
                    .addInterceptor(HeaderInterceptor())
                    .cookieJar(CookieJarImpl())
                val okHttpClient = builder.build()
                //组装retrofit
                mRetrofit = Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(baseUrl)
                    .build()
            }
            return mRetrofit
        }
        fun <T> create(service: Class<T>): T {
            return mRetrofit!!.create(service)
        }

        //根据文件路径猜测出文件类型
        private fun guessMimeType(path1: String): MediaType? {
            var path = path1
            val fileNameMap = URLConnection.getFileNameMap()
            path = path.replace("#", "")   //解决文件名中含有#号异常的问题
            var contentType: String? = fileNameMap.getContentTypeFor(path)
            if (contentType == null) {
                contentType = "application/octet-stream"
            }
            return contentType.toMediaTypeOrNull()
        }
    }



}