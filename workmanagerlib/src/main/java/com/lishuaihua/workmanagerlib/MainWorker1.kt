package com.lishuaihua.workmanagerlib

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

// 最简单的 执行任务
class MainWorker1(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    // 后台任务 并且 异步的
    override fun doWork(): Result {
        Log.d(TAG, "MainWorker1 doWork: run started ... ")
        try {
            Thread.sleep(8000) // 睡眠
        } catch (e: InterruptedException) {
            e.printStackTrace()
            return Result.failure() // 本次任务失败
        } finally {
            Log.d(TAG, "MainWorker1 doWork: run end ... ")
        }
        return Result.success() // 本次任务成功
    }

    companion object {
        private val TAG = MainWorker1::class.java.simpleName
    }
}