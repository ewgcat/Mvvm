package com.lishuaihua.baselib.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class TopRoundImageView : AppCompatImageView {
    //圆角弧度
    private val rids = floatArrayOf(12.0f, 12.0f, 12.0f, 12.0f, 0.0f, 0.0f, 0.0f, 0.0f)

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
    }

    override fun onDraw(canvas: Canvas) {
        val path = Path()
        val w = this.width
        val h = this.height
        //绘制圆角imageview
        path.addRoundRect(RectF(0f, 0f, w.toFloat(), h.toFloat()), rids, Path.Direction.CW)
        canvas.clipPath(path)
        super.onDraw(canvas)
    }
}