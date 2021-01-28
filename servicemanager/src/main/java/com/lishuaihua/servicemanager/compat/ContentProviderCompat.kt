package com.lishuaihua.servicemanager.compat

import android.content.ContentProviderClient
import android.net.Uri
import android.os.Build
import android.os.Bundle
import com.lishuaihua.servicemanager.ServiceManager
import com.lishuaihua.servicemanager.util.RefIectUtil

object ContentProviderCompat {
    @JvmStatic
    fun call(uri: Uri, method: String?, arg: String?, extras: Bundle?): Bundle? {
        val resolver = ServiceManager.sApplication?.contentResolver
        return if (Build.VERSION.SDK_INT >= 11) {
            resolver?.call(uri, method!!, arg, extras)
        } else {
            val client = resolver?.acquireContentProviderClient(uri)
                ?: throw IllegalArgumentException("Unknown URI $uri")
            try {
                val mContentProvider = RefIectUtil.getFieldObject(
                    client,
                    ContentProviderClient::class.java,
                    "mContentProvider"
                )
                if (mContentProvider != null) {
                    //public Bundle call(String method, String request, Bundle args)
                    var result: Any? = null
                    try {
                        result = RefIectUtil.invokeMethod(
                            mContentProvider,
                            Class.forName("android.content.IContentProvider"),
                            "call",
                            arrayOf(
                                String::class.java, String::class.java, Bundle::class.java
                            ),
                            arrayOf(method, arg, extras)
                        )
                    } catch (e: ClassNotFoundException) {
                        e.printStackTrace()
                    }
                    return result as Bundle?
                }
            } finally {
                client.release()
            }
            null
        }
    }
}