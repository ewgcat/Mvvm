package com.lishuaihua.home

import android.content.ComponentName
import android.content.Intent
import android.text.TextUtils
import com.google.auto.service.AutoService
import com.lishuaihua.baselib.base.BaseApplication
import com.lishuaihua.webview.ICallbackFromMainprocessToWebViewProcessInterface
import com.lishuaihua.webview.command.Command

@AutoService(Command::class)
class CommandOpenPage : Command {
    override fun name(): String {
        return "openPage"
    }

    override fun execute(
        parameters: Map<*, *>?,
        callback: ICallbackFromMainprocessToWebViewProcessInterface
    ) {
        val targetClass = parameters?.get("target_class").toString()
        if (!TextUtils.isEmpty(targetClass)) {
            val intent = Intent()
            intent.component = ComponentName(BaseApplication.instance, targetClass)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            BaseApplication.instance.startActivity(intent)
        }
    }
}