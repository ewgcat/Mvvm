package com.lishuaihua.webview

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.google.auto.service.AutoService
import com.lishuaihua.baselib.autoservice.IWebViewService
import com.lishuaihua.webview.utils.Constants

@AutoService(IWebViewService::class)
class WebViewServiceImpl : IWebViewService {
    override fun startWebViewActivity(
        context: Context,
        url: String,
        title: String,
        isShowActionBar: Boolean
    ) {
        if (context != null) {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra(Constants.TITLE, title)
            intent.putExtra(Constants.URL, url)
            intent.putExtra(Constants.IS_SHOW_ACTION_BAR, isShowActionBar)
            context.startActivity(intent)
        }
    }

    override fun getWebViewFragment(url: String, canNativeRefresh: Boolean): Fragment {
        return WebViewFragment.newInstance(url, canNativeRefresh)
    }

    override fun startDemoHtml(context: Context) {
        val intent = Intent(context, WebViewActivity::class.java)
        intent.putExtra(Constants.TITLE, "本地Demo测试页")
        intent.putExtra(Constants.URL, Constants.ANDROID_ASSET_URI + "demo.html")
        context.startActivity(intent)
    }
}