package com.lishuaihua.servicemanager

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.text.TextUtils
import android.util.Log
import androidx.annotation.RequiresApi
import com.lishuaihua.servicemanager.MethodRouter.routerToInstance
import com.lishuaihua.servicemanager.ProcessBinder
import com.lishuaihua.servicemanager.compat.BundleCompat.getBinder
import com.lishuaihua.servicemanager.compat.BundleCompat.putBinder
import com.lishuaihua.servicemanager.local.ServicePool.getService
import java.lang.reflect.Proxy
import java.util.concurrent.ConcurrentHashMap

/**
 * 利用ContentProvider实现同步跨进程调用，如果contentprovider所在进程退出，
 * 其他服务进程注册的binder和service信息会丢失
 */
class ServiceProvider : ContentProvider() {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun call(method: String, arg: String?, extras: Bundle?): Bundle? {
        if (Build.VERSION.SDK_INT >= 19) {
            Log.d("call", "callingPackage = $callingPackage")
        }
        Log.d(
            "call", "Thead : id = " + Thread.currentThread().id
                    + ", name = " + Thread.currentThread().name
                    + ", method = " + method
                    + ", arg = " + arg
        )
        if (method == REPORT_BINDER) {
            val pid = extras!!.getInt(PID)
            val iBinder = getBinder(extras, BINDER)
            processBinder[pid] = iBinder!!
            try {
                iBinder.linkToDeath({ removeAllRecordorForPid(pid) }, 0)
            } catch (e: RemoteException) {
                e.printStackTrace()
                processBinder.remove(pid)
            }
        } else if (method == PUBLISH_SERVICE) {
            val pid = extras!!.getInt(PID)
            val interfaceClass = extras.getString(INTERFACE)
            val binder = processBinder[pid]
            if (binder != null && binder.isBinderAlive) {
                val recorder = Recorder()
                recorder.pid = pid
                recorder.interfaceClass = interfaceClass
                allServiceList[arg!!] = recorder
            } else {
                allServiceList.remove(pid)
            }
            return null
        } else if (method == UNPUBLISH_SERVICE) {
            val pid = extras!!.getInt(PID)
            val name = extras.getString(NAME)
            if (TextUtils.isEmpty(name)) {
                removeAllRecordorForPid(pid)
            } else {
                allServiceList.remove(name)
                notifyClient(name)
            }
            return null
        } else if (method == CALL_SERVICE) {
            return routerToInstance(extras!!)
        } else if (method == QUERY_INTERFACE) {
            val bundle = Bundle()
            val recorder = allServiceList[arg]
            if (recorder != null) {
                bundle.putString(QUERY_INTERFACE_RESULT, recorder.interfaceClass)
            }
            return bundle
        } else if (method == QUERY_SERVICE) {
            if (allServiceList.containsKey(
                    arg
                )
            ) {
                val instance = getService(arg!!)
                val bundle = Bundle()
                return if (instance != null && !Proxy.isProxyClass(instance.javaClass)) {
                    bundle.putBoolean(QUERY_SERVICE_RESULT_IS_IN_PROVIDIDER_PROCESS, true)
                    bundle
                } else {
                    val recorder = allServiceList[arg]
                    if (recorder != null) {
                        val iBinder = processBinder[recorder.pid]
                        if (iBinder != null && iBinder.isBinderAlive) {
                            bundle.putBoolean(QUERY_SERVICE_RESULT_IS_IN_PROVIDIDER_PROCESS, false)
                            bundle.putString(
                                QUERY_SERVICE_RESULT_DESCRIPTOR,
                                ProcessBinder::class.java.name + "_" + recorder.pid
                            )
                            putBinder(bundle, QUERY_SERVICE_RESULT_BINDER, iBinder)
                            return bundle
                        }
                    }
                    null
                }
            }
        }
        return null
    }

    private fun removeAllRecordorForPid(pid: Int) {
        Log.w("ServiceProvider", "remove all service recordor for pid$pid")

        //服务提供方进程挂了,或者服务提供方进程主动通知清理服务, 则先清理服务注册表, 再通知所有客户端清理自己的本地缓存
        processBinder.remove(pid)
        val iterator: MutableIterator<Map.Entry<String?, Recorder>> =
            allServiceList.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (entry.value.pid == pid) {
                iterator.remove()
                notifyClient(entry.key)
            }
        }
    }

    private fun notifyClient(name: String?) {
        //通知持有服务的客户端清理缓存
        val intent = Intent(ServiceManager.ACTION_SERVICE_DIE_OR_CLEAR)
        intent.putExtra(NAME, name)
        ServiceManager.sApplication.sendBroadcast(intent)
    }

    class Recorder {
        var pid: Int? = null
        var interfaceClass: String? = null
    }

    override fun onCreate(): Boolean {
        return false
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        //doNothing
        return null
    }

    override fun getType(uri: Uri): String? {
        //doNothing
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        //doNothing
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        //doNothing
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        //doNothing
        return 0
    }

    companion object {
        const val REPORT_BINDER = "report_binder"
        const val PUBLISH_SERVICE = "publish_service"
        const val PUBLISH_SERVICE_BINDER = "publish_service_binder"
        const val UNPUBLISH_SERVICE = "unpublish_service"
        const val CALL_SERVICE = "call_service"
        const val QUERY_SERVICE = "query_service"
        const val QUERY_SERVICE_RESULT_IS_IN_PROVIDIDER_PROCESS =
            "query_service_result_is_in_provider_process"
        const val QUERY_SERVICE_RESULT_BINDER = "query_service_result_binder"
        const val QUERY_SERVICE_RESULT_DESCRIPTOR = "query_service_result_desciptor"
        const val QUERY_INTERFACE = "query_interface"
        const val QUERY_INTERFACE_RESULT = "query_interface_result"
        const val PID = "pid"
        const val BINDER = "binder"
        const val NAME = "name"
        const val INTERFACE = "interface"
        private var CONTENT_URI: Uri? = null

        //服务名：进程ID
        private val allServiceList = ConcurrentHashMap<String?, Recorder>()

        //进程ID：进程Binder
        private val processBinder = ConcurrentHashMap<Int?, IBinder?>()
        @JvmStatic
        fun buildUri(): Uri? {
            if (CONTENT_URI == null) {
                CONTENT_URI =
                    Uri.parse("content://" + ServiceManager.sApplication.packageName + ".svcmgr/call")
            }
            return CONTENT_URI
        }
    }
}