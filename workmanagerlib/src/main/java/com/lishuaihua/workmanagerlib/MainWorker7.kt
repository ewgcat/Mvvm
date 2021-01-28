package com.lishuaihua.workmanagerlib

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * （你怎么知道，他被杀掉后，还在后台执行？）写入文件的方式，向同学们证明 Derry说的 所言非虚
 * 后台任务7
 */
class MainWorker7     // 有构造函数
    (private val mContext: Context, private val workerParams: WorkerParameters) : Worker(
    mContext, workerParams
) {
    @SuppressLint("RestrictedApi")
    override fun doWork(): Result {
        Log.d(TAG, "MainWorker7 doWork: 后台任务执行了 started")
        try {
            Thread.sleep(8000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        // 获取SP
        val sp = applicationContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)

        // 获取 sp 里面的值
        var spIntValue = sp.getInt(SP_KEY, 0)
        sp.edit().putInt(SP_KEY, ++spIntValue).apply()
        Log.d(TAG, "MainWorker7 doWork: 后台任务执行了 end")
        return Result.Success() // 本地执行 doWork 任务时 成功 执行任务完毕
    }

    companion object {
        val TAG = MainWorker7::class.java.simpleName
        const val SP_NAME = "spNAME" // SP name
        const val SP_KEY = "spKEY" // KEY
    }
}