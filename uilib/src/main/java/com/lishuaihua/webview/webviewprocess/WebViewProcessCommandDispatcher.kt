package com.lishuaihua.webview.webviewprocess

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.RemoteException
import com.lishuaihua.baselib.base.BaseApplication
import com.lishuaihua.webview.ICallbackFromMainprocessToWebViewProcessInterface
import com.lishuaihua.webview.IWebviewProcessToMainProcessInterface
import com.lishuaihua.webview.mainprocess.MainProcessCommandService

class WebViewProcessCommandDispatcher : ServiceConnection {
    private var iWebviewProcessToMainProcessInterface: IWebviewProcessToMainProcessInterface? = null
    fun initAidlConnection() {
        val intent = Intent(BaseApplication.instance, MainProcessCommandService::class.java)
        BaseApplication.instance.bindService(intent, this, Context.BIND_AUTO_CREATE)
    }

    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        iWebviewProcessToMainProcessInterface =
            IWebviewProcessToMainProcessInterface.Stub.asInterface(service)
    }

    override fun onServiceDisconnected(name: ComponentName) {
        iWebviewProcessToMainProcessInterface = null
        initAidlConnection()
    }

    override fun onBindingDied(name: ComponentName) {
        iWebviewProcessToMainProcessInterface = null
        initAidlConnection()
    }

    fun executeCommand(commandName: String?, params: String?, baseWebView: BaseWebView) {
        if (iWebviewProcessToMainProcessInterface != null) {
            try {
                iWebviewProcessToMainProcessInterface!!.handleWebCommand(
                    commandName,
                    params,
                    object : ICallbackFromMainprocessToWebViewProcessInterface.Stub() {
                        @Throws(RemoteException::class)
                        override fun onResult(callbackname: String, response: String) {
                            baseWebView.handleCallback(callbackname, response)
                        }
                    })
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        private var sInstance: WebViewProcessCommandDispatcher? = null
        val instance: WebViewProcessCommandDispatcher?
            get() {
                if (sInstance == null) {
                    synchronized(WebViewProcessCommandDispatcher::class.java) {
                        sInstance = WebViewProcessCommandDispatcher()
                    }
                }
                return sInstance
            }
    }
}