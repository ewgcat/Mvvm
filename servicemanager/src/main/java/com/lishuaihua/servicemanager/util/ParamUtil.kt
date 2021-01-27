package com.lishuaihua.servicemanager.util

import android.os.Bundle
import com.lishuaihua.servicemanager.compat.BundleCompat.putObject

/**
 * Created by cailiming on 16/6/3.
 */
object ParamUtil {
    const val service_name = "service_name"
    const val method_name = "method_name"
    const val method_args_count = "method_args_count"
    const val result = "result"
    @JvmStatic
    fun wrapperParams(name: String?, methodName: String?, args: Array<Any?>?): Bundle {
        val params = Bundle()
        params.putString(service_name, name)
        params.putString(method_name, methodName)
        if (args != null && args.size > 0) {
            params.putInt(method_args_count, args.size)
            for (i in args.indices) {
                putObject(params, i.toString(), args[i]!!)
            }
        }
        return params
    }

    @JvmStatic
    fun unwrapperParams(extras: Bundle): Array<Any?>? {
        var params: Array<Any?>? = null
        val maxKey = extras.getInt(method_args_count)
        if (maxKey > 0) {
            params = arrayOfNulls(maxKey)
            for (i in 0 until maxKey) {
                params[i] = extras[i.toString()]
            }
        }
        return params
    }

    @JvmStatic
    fun getResult(bundle: Bundle?): Any? {
        return bundle?.get(result)
    }
}