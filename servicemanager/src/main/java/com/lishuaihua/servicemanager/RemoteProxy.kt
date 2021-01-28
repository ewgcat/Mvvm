package com.lishuaihua.servicemanager

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.lishuaihua.servicemanager.MethodRouter.routerToBinder
import com.lishuaihua.servicemanager.MethodRouter.routerToProvider
import com.lishuaihua.servicemanager.ServiceProvider.Companion.buildUri
import com.lishuaihua.servicemanager.compat.BundleCompat.getBinder
import com.lishuaihua.servicemanager.compat.ContentProviderCompat.call
import com.lishuaihua.servicemanager.util.ParamUtil.wrapperParams
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

object RemoteProxy {
    @JvmStatic
    fun getProxyService(name: String?, iFaceClassName: String?, classloader: ClassLoader): Any? {
        try {
            //classloader
            val clientClass = classloader.loadClass(iFaceClassName)
            return Proxy.newProxyInstance(classloader, arrayOf(clientClass),
                object : InvocationHandler {
                    var isInProviderProcess: Boolean? = null
                    var desciptpr: String? = null
                    var iBinder: IBinder? = null

                    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                    @Throws(Throwable::class)
                    override fun invoke(proxy: Any, method: Method, args: Array<Any?>): Any? {
                        val argsBundle = wrapperParams(name, method.toGenericString(), args)
                        if (isInProviderProcess == null) {
                            prepare(argsBundle)
                        }
                        if (java.lang.Boolean.TRUE == isInProviderProcess) {
                            return routerToProvider(name, argsBundle)!!
                        } else if (desciptpr != null && iBinder != null) {
                            return routerToBinder(desciptpr!!, iBinder!!, argsBundle)!!
                        } else {
                            //服务所在进程已死,重启服务进程可自动恢复
                            Log.w("RemoteProxy", "not active，service May Died！")
                        }
                        if (!method.returnType.isPrimitive) {
                            //是包装类，返回null
                            return  null
                        } else {
                            //不是包装类，默认返回值没法给，throws RemoteExecption
                            throw IllegalStateException("Service not active! Remote process may died")
                        }
                    }

                    @Throws(Throwable::class)
                    private fun prepare(argsBundle: Bundle) {
                        val queryResult = call(
                            buildUri()!!,
                            ServiceProvider.QUERY_SERVICE, name, argsBundle
                        )
                        if (queryResult != null) {
                            isInProviderProcess = queryResult.getBoolean(
                                ServiceProvider.QUERY_SERVICE_RESULT_IS_IN_PROVIDIDER_PROCESS,
                                false
                            )
                            iBinder =
                                getBinder(queryResult, ServiceProvider.QUERY_SERVICE_RESULT_BINDER)
                            desciptpr =
                                queryResult.getString(ServiceProvider.QUERY_SERVICE_RESULT_DESCRIPTOR)
                            if (iBinder != null) {
                                iBinder!!.linkToDeath({
                                    isInProviderProcess = null
                                    iBinder = null
                                    desciptpr = null
                                }, 0)
                            }
                        }
                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}