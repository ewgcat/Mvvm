package com.lishuaihua.home

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.google.auto.service.AutoService
import com.lishuaihua.baselib.base.BaseApplication
import com.lishuaihua.webview.ICallbackFromMainprocessToWebViewProcessInterface
import com.lishuaihua.webview.command.Command

@AutoService(Command::class)
class CommandShowToast : Command {
    override fun name(): String {
        return "showToast"
    }

    override fun execute(
        parameters: Map<*, *>?,
        callback: ICallbackFromMainprocessToWebViewProcessInterface
    ) {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            Toast.makeText(
                BaseApplication.instance,
                parameters?.get("message").toString(),
                Toast.LENGTH_SHORT
            ).show()

        }
    }

}