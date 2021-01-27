package com.lishuaihua.servicemanager

import android.os.*
import androidx.annotation.RequiresApi
import com.lishuaihua.servicemanager.compat.BundleCompat.putObject
import com.lishuaihua.servicemanager.compat.ContentProviderCompat.call
import com.lishuaihua.servicemanager.local.ServicePool.getService
import com.lishuaihua.servicemanager.util.ParamUtil
import com.lishuaihua.servicemanager.util.ParamUtil.getResult
import com.lishuaihua.servicemanager.util.ParamUtil.unwrapperParams
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Proxy


object MethodRouter {
    @JvmStatic
    fun routerToBinder(desciptpr: String, iBinder: IBinder, argsBundle: Bundle?): Any? {
        try {
            val bundle = transact(desciptpr, iBinder, argsBundle)
            return getResult(bundle)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
        return null
    }

    @JvmStatic
    fun routerToProvider(name: String?, argsBundle: Bundle?): Any? {
        val bundle =
            call(ServiceProvider.buildUri(), ServiceProvider.CALL_SERVICE, name, argsBundle)
        return getResult(bundle)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @JvmStatic
    fun routerToInstance(extras: Bundle): Bundle {
        val name = extras.getString(ParamUtil.service_name)
        val methodName = extras.getString(ParamUtil.method_name)
        val service = getService(name!!)
        var result: Any? = null
        if (service != null && !Proxy.isProxyClass(service.javaClass)) {
            val methods = service.javaClass.interfaces[0].declaredMethods
            if (methods != null) {
                for (m in methods) {
                    if (m.toGenericString() == methodName) {
                        try {
                            if (!m.isAccessible) {
                                m.isAccessible = true
                            }
                            result = m.invoke(service, unwrapperParams(extras))
                        } catch (e: IllegalAccessException) {
                            e.printStackTrace()
                        } catch (e: InvocationTargetException) {
                            e.printStackTrace()
                        }
                        break
                    }
                }
            }
        }
        val bundle = Bundle()
        putObject(bundle, ParamUtil.result, result!!)
        return bundle
    }

    @Throws(RemoteException::class)
    private fun transact(descriptor: String, remote: IBinder, param: Bundle?): Bundle? {
        val _data = Parcel.obtain()
        val _reply = Parcel.obtain()
        val _result: Bundle?
        _result = try {
            _data.writeInterfaceToken(descriptor)
            if (param != null) {
                _data.writeInt(1)
                param.writeToParcel(_data, 0)
            } else {
                _data.writeInt(0)
            }
            remote.transact(ProcessBinder.FIRST_CODE, _data, _reply, 0)
            _reply.readException()
            if (0 != _reply.readInt()) {
                Bundle.CREATOR.createFromParcel(_reply)
            } else {
                null
            }
        } finally {
            _reply.recycle()
            _data.recycle()
        }
        return _result
    }
}