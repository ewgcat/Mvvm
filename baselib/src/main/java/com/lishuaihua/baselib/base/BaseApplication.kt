package com.lishuaihua.baselib.base

import android.app.Application
import android.os.Build
import androidx.multidex.MultiDex
import com.alibaba.android.arouter.launcher.ARouter
import com.lishuaihua.baselib.BuildConfig
import com.lishuaihua.baselib.R
import com.lishuaihua.baselib.sp.SharedPreferencesManager
import com.lishuaihua.baselib.util.DateUtil
import com.lishuaihua.baselib.util.MD5Util
import com.lishuaihua.net.httputils.HttpUtils
import com.lishuaihua.servicemanager.ServiceManager
import com.lishuaihua.servicemanager.service.IAppInfoService
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import okhttp3.OkHttpClient

open class BaseApplication : Application(),IAppInfoService {
    override fun onCreate() {
        super.onCreate()
        instance = this
        MultiDex.install(applicationContext)
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
        //初始化manager
        ServiceManager.init(this)
//        ServiceManager.publishService(IAppInfoService.APP_INFO_SERVICE_NAME, BaseApplication::class.java.getName())
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
            "https://apigw.gialen.com/",
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

    override val applicationName: String?
        get() = "Mvvm"
    override val applciationVersionName: String?
        get() = "1.0.0"
    override val applicationVersionCode: String?
        get() = "1.0"
    override val applicationDebug: Boolean
        get() = BuildConfig.DEBUG

}