package com.lishuaihua.webview.mainprocess

import android.os.RemoteException
import com.google.gson.Gson
import com.lishuaihua.webview.ICallbackFromMainprocessToWebViewProcessInterface
import com.lishuaihua.webview.IWebviewProcessToMainProcessInterface
import com.lishuaihua.webview.command.Command
import java.util.*

class MainProcessCommandsManager private constructor() :
    IWebviewProcessToMainProcessInterface.Stub() {
    fun executeCommand(
        commandName: String,
        params: Map<*, *>?,
        callback: ICallbackFromMainprocessToWebViewProcessInterface?
    ) {
        mCommands[commandName]!!
            .execute(params, callback!!)
    }

    @Throws(RemoteException::class)
    override fun handleWebCommand(
        commandName: String,
        jsonParams: String,
        callback: ICallbackFromMainprocessToWebViewProcessInterface
    ) {
        instance!!.executeCommand(
            commandName,
            Gson().fromJson<Map<*, *>>(jsonParams, MutableMap::class.java),
            callback
        )
    }

    companion object {
        private var sInstance: MainProcessCommandsManager? = null
        private val mCommands = HashMap<String, Command>()
        val instance: MainProcessCommandsManager?
            get() {
                if (sInstance == null) {
                    synchronized(MainProcessCommandsManager::class.java) {
                        sInstance = MainProcessCommandsManager()
                    }
                }
                return sInstance
            }
    }

    init {
        val serviceLoader = ServiceLoader.load(
            Command::class.java
        )
        for (command in serviceLoader) {
            if (!mCommands.containsKey(command.name())) {
                mCommands[command.name()] = command
            }
        }
    }
}