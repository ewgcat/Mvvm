package com.lishuaihua.workmanagerlib

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * 后台任务5
 */
class MainWorker5     // 有构造函数
    (private val mContext: Context, private val workerParams: WorkerParameters) : Worker(
    mContext, workerParams
) {
    @SuppressLint("RestrictedApi")
    override fun doWork(): Result {
        Log.d(TAG, "MainWorker5 doWork: 后台任务执行了")
        return Result.Success() // 本地执行 doWork 任务时 成功 执行任务完毕
    }

    companion object {
        val TAG = MainWorker5::class.java.simpleName
    }
}