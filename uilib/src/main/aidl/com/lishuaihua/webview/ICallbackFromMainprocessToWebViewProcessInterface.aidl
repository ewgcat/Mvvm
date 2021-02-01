// ICallbackFromMainprocessToWebViewProcessInterface.aidl
package com.lishuaihua.webview;

interface ICallbackFromMainprocessToWebViewProcessInterface {
    void onResult(String callbackname, String response);
}
