package com.lishuaihua.imageloader.svg.utils

import android.graphics.Canvas
import java.lang.reflect.Method

/**
 * "Aaand it's gone": Canvas#save(int) has been removed from sdk-28,
 * so this helper classes uses reflection to access the API on older devices.
 */
object CanvasLegacy {
     var MATRIX_SAVE_FLAG = 0
    private var SAVE: Method? = null
    @JvmStatic
    fun save(canvas: Canvas?, saveFlags: Int) {
        try {
            SAVE!!.invoke(canvas, saveFlags)
        } catch (e: Throwable) {
            throw sneakyThrow(e)
        }
    }

    private fun sneakyThrow(t: Throwable?): RuntimeException {
        if (t == null) throw NullPointerException("t")
        return sneakyThrow0(t)
    }

    @Throws(Throwable::class)
    private fun <T : Throwable?> sneakyThrow0(t: Throwable): T {
        throw t as T as Throwable
    }

    init {
        try {
            MATRIX_SAVE_FLAG = Canvas::class.java.getField("MATRIX_SAVE_FLAG")[null] as Int
            SAVE = Canvas::class.java.getMethod("save", Int::class.javaPrimitiveType)
        } catch (e: Throwable) {
            throw sneakyThrow(e)
        }
    }
}

