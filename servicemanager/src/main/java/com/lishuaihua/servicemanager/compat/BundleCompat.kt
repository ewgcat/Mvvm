package com.lishuaihua.servicemanager.compat

import android.os.BaseBundle
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.ArrayMap
import androidx.annotation.RequiresApi
import com.lishuaihua.servicemanager.util.RefIectUtil


object BundleCompat {
    @JvmStatic
    fun getBinder(bundle: Bundle, key: String?): IBinder? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            bundle.getBinder(key)
        } else {
            RefIectUtil.invokeMethod(
                bundle, Bundle::class.java, "getIBinder", arrayOf<Class<*>?>(
                    String::class.java
                ), arrayOf<Any?>(key)
            ) as IBinder
        }
    }

    @JvmStatic
    fun putBinder(bundle: Bundle, key: String?, iBinder: IBinder?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            bundle.putBinder(key, iBinder)
        } else {
            RefIectUtil.invokeMethod(
                bundle, Bundle::class.java, "putIBinder", arrayOf(
                    String::class.java, IBinder::class.java
                ), arrayOf(key, iBinder)
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @JvmStatic
    fun putObject(bundle: Bundle?, key: String, value: Any) {
        if (Build.VERSION.SDK_INT < 19) {
            RefIectUtil.invokeMethod(
                bundle,
                Bundle::class.java,
                "unparcel",
                null as Array<Class<*>?>,
                null as Array<Any?>
            )
            val mMap = RefIectUtil.getFieldObject(
                bundle,
                Bundle::class.java,
                "mMap"
            ) as MutableMap<String, Any>
            mMap[key] = value
        } else if (Build.VERSION.SDK_INT == 19) {
            RefIectUtil.invokeMethod(
                bundle,
                Bundle::class.java,
                "unparcel",
                null as Array<Class<*>?>,
                null as Array<Any?>
            )
            val mMap = RefIectUtil.getFieldObject(
                bundle,
                Bundle::class.java,
                "mMap"
            ) as ArrayMap<String, Any>
            mMap[key] = value
        } else if (Build.VERSION.SDK_INT > 19) {
            RefIectUtil.invokeMethod(
                bundle,
                BaseBundle::class.java,
                "unparcel",
                null as Array<Class<*>?>,
                null as Array<Any?>
            )
            val mMap = RefIectUtil.getFieldObject(
                bundle,
                BaseBundle::class.java,
                "mMap"
            ) as ArrayMap<String, Any>
            mMap[key] = value
        }
    }
}