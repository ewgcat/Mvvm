package com.gialen.baselib.util

import android.util.Log
import com.gialen.baselib.BuildConfig

class Logger {
    fun setTag(tag: String) {
        TAG = tag
    }
    companion object {
        private const val DEFAULT_TAG = "Gialen"
        private  var TAG = DEFAULT_TAG
        private var debug = BuildConfig.DEBUG
        @JvmStatic
        fun v(message: String?) {
            if (debug) {
                Log.v(TAG, message!!)
            }
        }
        @JvmStatic
        fun d(message: String?) {
            if (debug) {
                Log.d(TAG, message!!)
            }
        }
        @JvmStatic
        fun i(message: String?) {
            if (debug) {
                Log.i(TAG, message!!)
            }
        }
        @JvmStatic
        fun w(message: String?) {
            if (debug) {
                Log.w(TAG, message!!)
            }
        }
        @JvmStatic
        fun e(message: String?) {
            if (debug) {
                Log.e(TAG, message!!)
            }
        }
        @JvmStatic
        fun wtf(message: String?) {
            if (debug) {
                Log.wtf(TAG, message!!)
            }
        }
        @JvmStatic
        fun v(tag: String?, message: String?) {
            if (debug) {
                Log.v(tag, message!!)
            }
        }
        @JvmStatic
        fun d(tag: String?, message: String?) {
            if (debug) {
                Log.d(tag, message!!)
            }
        }

        @JvmStatic
        fun i(tag: String?, message: String?) {
            if (debug) {
                Log.i(tag, message!!)
            }
        }

        @JvmStatic
        fun w(tag: String?, message: String?) {
            if (debug) {
                Log.w(tag, message!!)
            }
        }

        @JvmStatic
        fun e(tag: String?, message: String?) {
            if (debug) {
                Log.e(tag, message!!)
            }
        }
        @JvmStatic
        fun wtf(tag: String?, message: String?) {
            if (debug) {
                Log.wtf(tag, message!!)
            }
        }
    }
}