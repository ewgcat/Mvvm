package com.lishuaihua.servicemanager.local

import com.lishuaihua.servicemanager.local.ServiceFetcher

/**
 * Created by cailiming on 16/1/1.
 */
abstract class ServiceFetcher {
    var mServiceId = 0
    var mGroupId: String? = null
    private var mCachedInstance: Any? = null
    val service: Any
        get() {
            synchronized(this@ServiceFetcher) {
                val service = mCachedInstance
                return service ?: createService(mServiceId).also { mCachedInstance = it }
            }
        }

    abstract fun createService(serviceId: Int): Any
}