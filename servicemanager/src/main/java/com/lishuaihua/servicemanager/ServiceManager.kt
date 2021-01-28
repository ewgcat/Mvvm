package com.lishuaihua.servicemanager

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Process
import androidx.annotation.NonNull
import com.lishuaihua.servicemanager.ProcessBinder
import com.lishuaihua.servicemanager.RemoteProxy.getProxyService
import com.lishuaihua.servicemanager.ServiceProvider.Companion.buildUri
import com.lishuaihua.servicemanager.compat.BundleCompat.putBinder
import com.lishuaihua.servicemanager.compat.ContentProviderCompat.call
import com.lishuaihua.servicemanager.local.ServicePool
import com.lishuaihua.servicemanager.local.ServicePool.ClassProvider
import com.lishuaihua.servicemanager.local.ServicePool.registerClass
import com.lishuaihua.servicemanager.local.ServicePool.registerInstance
import com.lishuaihua.servicemanager.local.ServicePool.unRegister

object ServiceManager {
    const val ACTION_SERVICE_DIE_OR_CLEAR = "com.lishuaihua.action.SERVICE_DIE_OR_CLEAR"
    var sApplication: Application? = null
    fun init(application: Application?) {
        sApplication = application
        val argsBundle = Bundle()
        val pid = Process.myPid()
        argsBundle.putInt(ServiceProvider.PID, pid)
        //为每个进程发布一个binder
        putBinder(
            argsBundle, ServiceProvider.BINDER, ProcessBinder(
                ProcessBinder::class.java.name + "_" + pid
            )
        )
        call(
            buildUri()!!,
            ServiceProvider.REPORT_BINDER, null, argsBundle
        )
        sApplication!!.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                //服务进程挂掉以后 或者服务进程主动通知清理时,移除客户端的代理缓存
                unRegister(intent.getStringExtra(ServiceProvider.NAME)!!)
            }
        }, IntentFilter(ACTION_SERVICE_DIE_OR_CLEAR))
    }

    fun getService(name: String?): Any? {
        return getService(name, ServiceManager::class.java.classLoader)
    }

    /**
     *
     * @param name
     * @param interfaceClassloader
     * @return
     */
    fun getService(name: String?, interfaceClassloader: ClassLoader?): Any? {

        //首先在当前进程内查询
        var service = ServicePool.getService(name!!)
        if (service == null) {
            //向远端器查询
            val bundle = call(
                buildUri()!!,
                ServiceProvider.QUERY_INTERFACE, name, null
            )
            if (bundle != null) {
                val interfaceClassName = bundle.getString(ServiceProvider.QUERY_INTERFACE_RESULT)
                if (interfaceClassName != null) {
                    service = getProxyService(name, interfaceClassName, interfaceClassloader!!)
                    //缓存Proxy到本地
                    if (service != null) {
                        registerInstance(name, service)
                    }
                }
            }
        }
        return service
    }

    /**
     * 给当前进程发布一个服务, 发布后其他进程可使用此服务
     */
    @JvmOverloads
    fun publishService(
        @NonNull   name: String,
        className: String?,
        classloader: ClassLoader = ServiceManager::class.java.classLoader
    ) {
        publishService(name, object : ClassProvider() {
            override val serviceInstance: Any?
                get() {
                    try {
                        return classloader.loadClass(className).newInstance()
                    } catch (e: ClassNotFoundException) {
                        e.printStackTrace()
                    } catch (e: InstantiationException) {
                        e.printStackTrace()
                    } catch (e: IllegalAccessException) {
                        e.printStackTrace()
                    }
                    return null
                }
            override val interfaceName: String?
                get() {
                    try {
                        return classloader.loadClass(className).interfaces[0].name
                    } catch (e: ClassNotFoundException) {
                        e.printStackTrace()
                    }
                    return null
                }
        })
    }

    /**
     * 给当前进程发布一个服务, 发布后其他进程可使用此服务
     */
    fun publishService(@NonNull name: String, provider: ClassProvider) {

        //先缓存到本地
        registerClass(name, provider)
        val pid = Process.myPid()
        val argsBundle = Bundle()
        argsBundle.putInt(ServiceProvider.PID, pid)

        //classLoader
        val serviceInterfaceClassName = provider.interfaceName
        argsBundle.putString(ServiceProvider.INTERFACE, serviceInterfaceClassName)
        //再发布到远端
        call(
            buildUri()!!,
            ServiceProvider.PUBLISH_SERVICE, name, argsBundle
        )
    }

    /**
     * 清理当前进程发布的所有服务
     */
    fun unPublishAllService() {
        val pid = Process.myPid()
        val argsBundle = Bundle()
        argsBundle.putInt(ServiceProvider.PID, pid)
        call(
            buildUri()!!,
            ServiceProvider.UNPUBLISH_SERVICE, null, argsBundle
        )
    }

    fun unPublishService(name: String?) {
        val pid = Process.myPid()
        val argsBundle = Bundle()
        argsBundle.putInt(ServiceProvider.PID, pid)
        argsBundle.putString(ServiceProvider.NAME, name)
        call(
            buildUri()!!,
            ServiceProvider.UNPUBLISH_SERVICE, null, argsBundle
        )
    }
}