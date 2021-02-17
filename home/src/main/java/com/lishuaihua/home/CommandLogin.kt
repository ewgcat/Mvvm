package com.lishuaihua.home

import android.os.RemoteException
import android.util.Log
import com.google.auto.service.AutoService
import com.google.gson.Gson
import com.lishuaihua.baselib.autoservice.IUserCenterService
import com.lishuaihua.baselib.autoservice.ServiceLoaderHelper
import com.lishuaihua.baselib.eventbus.LoginEvent
import com.lishuaihua.webview.ICallbackFromMainprocessToWebViewProcessInterface
import com.lishuaihua.webview.command.Command
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.*

@AutoService(Command::class)
class CommandLogin : Command {
    var iUserCenterService = ServiceLoaderHelper.load(
        IUserCenterService::class.java
    )
    var callback: ICallbackFromMainprocessToWebViewProcessInterface? = null
    var callbacknameFromNativeJs: String? = null
    override fun name(): String {
        return "login"
    }

    override fun execute(
        parameters: Map<*, *>?,
        callback: ICallbackFromMainprocessToWebViewProcessInterface
    ) {
        Log.d("CommandLogin", Gson().toJson(parameters))
        if (iUserCenterService != null && !iUserCenterService!!.isLogined) {
            iUserCenterService!!.login()
            this.callback = callback
            callbacknameFromNativeJs = parameters?.get("callbackname").toString()
        }
    }

    @Subscribe
    fun onMessageEvent(event: LoginEvent) {
        Log.d("CommandLogin", event.userName)
        val map: HashMap<String, String> = HashMap<String, String>()
        map.put("accountName",event.userName)
        if (callback != null) {
            try {
                callback!!.onResult(callbacknameFromNativeJs, Gson().toJson(map))
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }
    }

    init {
        EventBus.getDefault().register(this)
    }
}