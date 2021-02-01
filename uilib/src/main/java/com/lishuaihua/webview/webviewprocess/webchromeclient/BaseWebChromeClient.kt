package com.lishuaihua.webview.webviewprocess.webchromeclient

import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.lishuaihua.webview.WebViewCallBack

class BaseWebChromeClient(private val mWebViewCallBack: WebViewCallBack?) : WebChromeClient() {
    override fun onReceivedTitle(view: WebView, title: String) {
        mWebViewCallBack?.updateTitle(title)
            ?: Log.e(TAG, "WebViewCallBack is null.")
    }

    /**
     * Report a JavaScript console message to the host application. The ChromeClient
     * should override this to process the log message as they see fit.
     * @param consoleMessage Object containing details of the console message.
     * @return `true` if the message is handled by the client.
     */
    override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
        // Call the old version of this function for backwards compatability.
        Log.d(TAG, consoleMessage.message())
        return super.onConsoleMessage(consoleMessage)
    }

    companion object {
        private const val TAG = "BaseWebChromeClient"
    }
}