package com.lishuaihua.baselib.ext

import android.app.Activity
import androidx.viewbinding.ViewBinding


inline fun <T, R> T.dowithTry(block: (T) -> R) {
    try {
        block(this)
    } catch (e: Throwable) {
        e.printStackTrace()
    }
}
