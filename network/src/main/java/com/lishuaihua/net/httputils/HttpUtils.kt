package com.lishuaihua.net.httputils

import android.content.Context
import android.util.Log
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.lishuaihua.net.BuildConfig
import com.lishuaihua.net.dns.JackDns
import com.lishuaihua.net.log.HttpLogger
import com.lishuaihua.net.ssl.HTTPSCerUtils
import com.lishuaihua.net.support.CookieJarImpl
import com.lishuaihua.net.support.HeaderInterceptor
import com.moczul.ok2curl.CurlInterceptor
import com.moczul.ok2curl.logger.Loggable
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
                    val loggingInterceptor = HttpLoggingInterceptor(
                        HttpLogger()
                    )
                    if (BuildConfig.DEBUG) {
                        // 测试
                        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                    } else {
                        // 打包
                        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE)
                    }
                    builder.addInterceptor(loggingInterceptor)
                    builder.addInterceptor(CurlInterceptor { message -> Log.d("curl", message!!) })
                    if (BuildConfig.DEBUG) {
                        val chuckerInterceptor = ChuckerInterceptor(context)
                        builder.addInterceptor(chuckerInterceptor)
                    }
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
                    .addInterceptor(CacheInterceptor(context))
                    .cookieJar(CookieJarImpl())

                builder.dns(JackDns())
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


    }
}