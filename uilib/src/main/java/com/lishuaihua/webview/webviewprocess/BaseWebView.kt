package com.lishuaihua.webview.webviewprocess

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.google.gson.Gson
import com.lishuaihua.webview.WebViewCallBack
import com.lishuaihua.webview.bean.JsParam
import com.lishuaihua.webview.webviewprocess.settings.WebViewDefaultSettings.Companion.instance
import com.lishuaihua.webview.webviewprocess.webchromeclient.BaseWebChromeClient
import com.lishuaihua.webview.webviewprocess.webviewclient.BaseWebViewClient

class BaseWebView : WebView {
    constructor(context: Context?) : super(context!!) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
        init()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(
        context!!, attrs, defStyleAttr, defStyleRes
    ) {
        init()
    }

    fun init() {
        WebViewProcessCommandDispatcher.instance!!.initAidlConnection()
        instance.setSettings(this)
        addJavascriptInterface(this, "basewebview")
    }

    fun registerWebViewCallBack(webViewCallBack: WebViewCallBack?) {
        webViewClient = BaseWebViewClient(webViewCallBack)
        webChromeClient = BaseWebChromeClient(webViewCallBack)
    }

    @JavascriptInterface
    fun takeNativeAction(jsParam: String?) {
        Log.i(TAG, jsParam!!)
        if (!TextUtils.isEmpty(jsParam)) {
            val jsParamObject = Gson().fromJson(jsParam, JsParam::class.java)
            if (jsParamObject != null) {
                WebViewProcessCommandDispatcher.instance!!
                    .executeCommand(jsParamObject.name, Gson().toJson(jsParamObject.param), this)
            }
        }
    }

    fun handleCallback(callbackname: String, response: String) {
        if (!TextUtils.isEmpty(callbackname) && !TextUtils.isEmpty(response)) {
            post {
                val jscode = "javascript:webviewjs.callback('$callbackname',$response)"
                Log.i(TAG, jscode)
                evaluateJavascript(jscode, null)
            }
        }
    }

    companion object {
        const val TAG = "BaseWebView"
    }
}