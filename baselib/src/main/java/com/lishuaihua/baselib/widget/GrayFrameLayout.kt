package com.lishuaihua.baselib.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.FrameLayout

class GrayFrameLayout(context: Context?, attrs: AttributeSet?) : FrameLayout(
    context!!, attrs
) {
    private val mPaint = Paint()
    override fun dispatchDraw(canvas: Canvas) {
        canvas.saveLayer(null, mPaint)
        super.dispatchDraw(canvas)
        canvas.restore()
    }

    override fun draw(canvas: Canvas) {
        canvas.saveLayer(null, mPaint)
        super.draw(canvas)
        canvas.restore()
    }

    init {
        val cm = ColorMatrix()
        cm.setSaturation(0f)
        mPaint.colorFilter = ColorMatrixColorFilter(cm)
    }
}