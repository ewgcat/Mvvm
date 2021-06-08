package com.lishuaihua.index.hotarea

import android.content.Context
import android.os.Build
import android.view.View
import android.webkit.*
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.lishuaihua.index.R
import com.lishuaihua.index.bean.IndexData


class Hot_AreaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var webView: WebView
    lateinit var context: Context

    init {
        webView = itemView.findViewById(R.id.web_view)
    }

    fun bindView(context: Context, indexData: IndexData) {
        this.context = context

        val webSettings = webView.settings
        webSettings.domStorageEnabled = true//主要是这句
        webSettings.javaScriptEnabled = true//启用js
        webSettings.blockNetworkImage = false//解决图片不显示
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.loadsImagesAutomatically = true

        WebView.setWebContentsDebuggingEnabled(true);
        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }   //该方法解决的问题是打开浏览器不调用系统浏览器，直接用webview打开
        webView.setWebChromeClient(WebChromeClient())
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }

        webView.addJavascriptInterface(this, "android")
        webView.loadUrl(indexData.url)
    }

    @JavascriptInterface
    fun jumpNextPage(json: String) {

    }



}

