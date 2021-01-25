package com.lishuaihua.baselib.base

import android.app.Application
import android.os.Build
import androidx.multidex.MultiDex
import com.lishuaihua.baselib.BuildConfig
import com.lishuaihua.baselib.R
import com.lishuaihua.baselib.util.DateUtil
import com.lishuaihua.baselib.util.MD5Util
import com.lishuaihua.baselib.sp.SharedPreferencesManager
import com.lishuaihua.net.httputils.HttpUtils
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import okhttp3.OkHttpClient

open class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        MultiDex.install(applicationContext)
        val req_date =
            DateUtil.timeStamp2Date2(DateUtil.timestamp_13.toString() + "", "yyyyMMddHH24MMSS")
        val sessionId = MD5Util.MD5(req_date + 2 + "gialen_APP")
        SharedPreferencesManager.getInstance(instance)
            .saveString(SharedPreferencesManager.SPCommons.SESSIONID, sessionId)
        val builder = OkHttpClient.Builder()
        builder.addInterceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .addHeader("platfrom", "1")
                .addHeader("terminal", "2")
                .addHeader("sessionId", sessionId)
                .addHeader("appVersion", "1.0.0")
                .addHeader("mobileModel", Build.BRAND + " " + Build.MODEL)
                .addHeader("IMEI", "udid")
                .build()
            chain.proceed(request)
        }
        HttpUtils.init(
            applicationContext,
            "http://cs-jiaomigo.gialen.com/gateway/",
            R.raw.gialen_2020_6_9,
            builder,
            BuildConfig.DEBUG
        )
        initSmartRefreshLayout()
    }
    private fun initSmartRefreshLayout() {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { instance, _ ->
            ClassicsHeader(instance)
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { instance, _ ->
            ClassicsFooter(instance)
        }
    }
    companion object {
        private val TAG = BaseApplication::class.java.simpleName

        @get:Synchronized
        lateinit var instance: BaseApplication


    }
}