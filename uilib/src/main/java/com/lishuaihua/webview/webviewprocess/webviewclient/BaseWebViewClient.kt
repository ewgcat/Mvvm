package com.lishuaihua.webview.webviewprocess.webviewclient

import android.graphics.Bitmap
import android.util.Log
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.NonNull
import com.lishuaihua.webview.WebViewCallBack

class BaseWebViewClient(private val mWebViewCallBack: WebViewCallBack?) : WebViewClient() {
    override fun onPageStarted(@NonNull view: WebView,@NonNull url: String, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        mWebViewCallBack?.pageStarted(url)
    }

    override fun onPageFinished(@NonNull view: WebView,@NonNull url: String) {
        super.onPageFinished(view, url)
        mWebViewCallBack?.pageFinished(url)

    }

    override fun onReceivedError(
        view: WebView,
        request: WebResourceRequest,
        error: WebResourceError
    ) {
        super.onReceivedError(view, request, error)
        mWebViewCallBack?.onError()

    }

    companion object {
        private const val TAG = "BaseWebViewClient"
    }
}