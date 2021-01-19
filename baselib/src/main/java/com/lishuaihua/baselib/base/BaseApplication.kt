package com.lishuaihua.baselib.base

import android.app.Application
import androidx.multidex.MultiDex

open class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        MultiDex.install(applicationContext)
    }

    companion object {
        private val TAG = BaseApplication::class.java.simpleName

        @get:Synchronized
        lateinit var instance: BaseApplication

    }
}