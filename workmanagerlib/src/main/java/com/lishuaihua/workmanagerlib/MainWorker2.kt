package com.lishuaihua.workmanagerlib

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * 数据 互相传递
 * 后台任务
 */
class MainWorker2(private val mContext: Context, private val workerParams: WorkerParameters) :
    Worker(
        mContext, workerParams
    ) {
    @SuppressLint("RestrictedApi")
    override fun doWork(): Result {
        Log.d(TAG, "MainWorker2 doWork: 后台任务执行了")

        // 接收 MainActivity传递过来的数据
        val dataString = workerParams.inputData.getString("Derry")
        Log.d(TAG, "MainWorker2 doWork: 接收Activity传递过来的数据:$dataString")

        // 反馈数据 给 MainActivity
        // 把任务中的数据回传到activity中
        val outputData = Data.Builder().putString("Derry", "三分归元气").build()

        // return new Result.Failure(); // 本地执行 doWork 任务时 失败
        // return new Result.Retry(); // 本地执行 doWork 任务时 重试一次
        // return new Result.Success(); // 本地执行 doWork 任务时 成功 执行任务完毕
        return Result.Success(outputData)
    }

    companion object {
        @JvmField
        val TAG = MainWorker2::class.java.simpleName
    }
}