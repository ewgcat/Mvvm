package com.lishuiahua.webview_module

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.lishuaihua.baselib.base.BaseActivity
import com.lishuaihua.baselib.binding.binding
import com.lishuaihua.net.httputils.BaseViewModel
import com.lishuiahua.webview.command.Command
import com.lishuiahua.webview.command.CommandsManager
import com.lishuiahua.webview.command.ResultBack
import com.lishuiahua.webview.remotewebview.BaseWebView
import com.lishuiahua.webview.utils.AidlError
import com.lishuiahua.webview.utils.WebConstants
import com.lishuiahua.webview_module.databinding.ActivityWebViewTestBinding
import java.util.*

@Route(path = "/webview_module/test")
class WebViewTestActivity : BaseActivity<BaseViewModel>() {

    private val binding: ActivityWebViewTestBinding by binding()
    override fun getLayoutResId(): Int =R.layout.activity_web_view_test

    override fun doCreateView(savedInstanceState: Bundle?) {
        binding.navigationBar.setTitle("WebView 测试页面")
        binding.navigationBar.setBackClickListener(this)
       binding.openWeb1.setOnClickListener {
            WebActivity.startCommonWeb(
                this@WebViewTestActivity,
                "腾讯网",
                "https://xw.qq.com/?f=qqcom",
                WebConstants.LEVEL_BASE
            )
        }
        CommandsManager.getInstance()
            .registerCommand(WebConstants.LEVEL_ACCOUNT, appDataProviderCommand)
        CommandsManager.getInstance().registerCommand(WebConstants.LEVEL_BASE, pageRouterCommand)
        binding.openWeb2.setOnClickListener {
            WebActivity.startCommonWeb(
                this@WebViewTestActivity,
                "AIDL测试",
                BaseWebView.CONTENT_SCHEME + "aidl.html",
                WebConstants.LEVEL_ACCOUNT
            )
        }
        binding.openWeb3.setOnClickListener { // for account level
            val accountInfo = HashMap<String, String>()
            accountInfo["username"] = "TestAccount"
            accountInfo["access_token"] = "880fed4ca2aabd20ae9a5dd774711de2"
            accountInfo["phone"] = "+8613989898898"
            WebActivity.startAccountWeb(
                this@WebViewTestActivity,
                "百度",
                "http://www.baidu.com",
                WebConstants.LEVEL_ACCOUNT,
                accountInfo
            )
        }
    }

    /**
     * 页面路由
     */
    private val pageRouterCommand: Command = object : Command {
        override fun name(): String {
            return "newPage"
        }

        override fun exec(context: Context, params: Map<*, *>, resultBack: ResultBack) {
            val newUrl = params["url"].toString()
            val title = params["title"] as String?
        }
    }

    // 获取native data
    private val appDataProviderCommand: Command = object : Command {
        override fun name(): String {
            return "appDataProvider"
        }

        override fun exec(context: Context, params: Map<*, *>, resultBack: ResultBack) {
            try {
                var callbackName = ""
                if (params["type"] == null) {
                    val aidlError = AidlError(
                        WebConstants.ERRORCODE.ERROR_PARAM,
                        WebConstants.ERRORMESSAGE.ERROR_PARAM
                    )
                    resultBack.onResult(WebConstants.FAILED, name(), aidlError)
                    return
                }
                if (params[WebConstants.WEB2NATIVE_CALLBACk] != null) {
                    callbackName = params[WebConstants.WEB2NATIVE_CALLBACk].toString()
                }
                val type = params["type"].toString()
                val map: HashMap<String, String> = HashMap<String, String>()
                when (type) {
                    "account" -> {
                        map["accountId"] = "test123456"
                        map["accountName"] = "lishuaihua"
                    }
                }
                if (!TextUtils.isEmpty(callbackName)) {
                    map[WebConstants.NATIVE2WEB_CALLBACK] = callbackName
                }
                resultBack.onResult(WebConstants.SUCCESS, name(), map)
            } catch (e: Exception) {
                e.printStackTrace()
                val aidlError = AidlError(
                    WebConstants.ERRORCODE.ERROR_PARAM,
                    WebConstants.ERRORMESSAGE.ERROR_PARAM
                )
                resultBack.onResult(WebConstants.FAILED, name(), aidlError)
            }
        }
    }

}