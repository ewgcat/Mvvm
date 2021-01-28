package com.lishuaihua.servicemanager.local

import android.os.Process
import android.util.Log
import java.util.*

/**
 * Created by cailiming on 16/1/1.
 */
object ServicePool {
    private val TAG = ServicePool::class.java.simpleName
    private val SYSTEM_SERVICE_MAP = Hashtable<String, ServiceFetcher>()
    @JvmStatic
    @Synchronized
    fun registerClass(name: String, provider: ClassProvider) {
        Log.d(TAG, "registerClass service $name")
        if (!SYSTEM_SERVICE_MAP.containsKey(name)) {
            val fetcher: ServiceFetcher = object : ServiceFetcher() {
                override fun createService(serviceId: Int): Any {
                    val `object` = provider.serviceInstance
                    mGroupId = Process.myPid().toString()
                    Log.d(TAG, "create service instance @ pid " + Process.myPid())
                    return `object`
                }
            }
            fetcher.mServiceId++
            SYSTEM_SERVICE_MAP[name] = fetcher
        }
    }

    @JvmStatic
    @Synchronized
    fun registerInstance(name: String, service: Any) {
        Log.d(TAG, "registerInstance service $name @ $service")
        val faces = service.javaClass.interfaces
        if (faces == null || faces.size == 0) {
            return
        }
        if (!SYSTEM_SERVICE_MAP.containsKey(name)) {
            val fetcher: ServiceFetcher = object : ServiceFetcher() {
                override fun createService(serviceId: Int): Any {
                    mGroupId = Process.myPid().toString()
                    Log.d(TAG, "create service instance @ pid " + Process.myPid())
                    return service
                }
            }
            fetcher.mServiceId++
            SYSTEM_SERVICE_MAP[name] = fetcher
        }
    }

    @JvmStatic
    fun getService(name: String): Any? {
        val fetcher = SYSTEM_SERVICE_MAP[name]
        return fetcher?.service
    }

    @JvmStatic
    fun unRegister(name: String) {
        Log.d(TAG, "unRegister service $name")
        SYSTEM_SERVICE_MAP.remove(name)
    }

    abstract class ClassProvider {
        abstract val serviceInstance: Any?
        abstract val interfaceName: String?
    }
}