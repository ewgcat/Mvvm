package com.lishuaihua.webview.command

import com.lishuaihua.webview.ICallbackFromMainprocessToWebViewProcessInterface

interface Command {
    fun name(): String
    fun execute(
        parameters: Map<*, *>?,
        callback: ICallbackFromMainprocessToWebViewProcessInterface
    )
}