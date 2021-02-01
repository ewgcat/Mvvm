package com.lishuaihua.baselib.base

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Process
import android.webkit.WebView
import androidx.multidex.MultiDex
import com.alibaba.android.arouter.launcher.ARouter
import com.gialen.baselib.util.MD5Util
import com.kingja.loadsir.core.LoadSir
import com.lishuaihua.baselib.BuildConfig
import com.lishuaihua.baselib.R
import com.lishuaihua.baselib.loadsir.*
import com.lishuaihua.baselib.sp.SharedPreferencesManager
import com.lishuaihua.baselib.util.DateUtil
import com.lishuaihua.net.httputils.HttpUtils
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tencent.bugly.Bugly
import okhttp3.OkHttpClient

open class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        MultiDex.install(applicationContext)
        Bugly.init(applicationContext,"c3010b29f1",BuildConfig.DEBUG)

        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
        val req_date =
            DateUtil.timeStamp2Date2(DateUtil.timestamp_13.toString() + "", "yyyyMMddHH24MMSS")
        val sessionId = MD5Util.MD5(req_date + 2 + "gialen_APP")
        SharedPreferencesManager.getInstance(instance)!!.saveString(SharedPreferencesManager.SPCommons.SESSIONID, sessionId)
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val processName = getProcessName(this)
            val packageName = this.packageName
            if (packageName != processName) {
                WebView.setDataDirectorySuffix(processName!!)
            }
        }
        LoadSir.beginBuilder()
            .addCallback(ErrorCallback())
            .addCallback(EmptyCallback())
            .addCallback(LoadingCallback())
            .addCallback(TimeoutCallback())
            .addCallback(CustomCallback())
            .setDefaultCallback(LoadingCallback::class.java)
            .commit()
    }

    open fun getProcessName(context: Context): String? {
        var manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (processInfo in manager.runningAppProcesses) {
            if (processInfo.pid == Process.myPid()) {
                return processInfo.processName
            }
        }
        return null
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