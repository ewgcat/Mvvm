package com.lishuaihua.floatwindow

import android.content.Context
import android.view.Gravity
import java.lang.Exception

class NotFullScreenTouchDisableFloatWindowFloatWindow(context: Context?) : BaseFloatWindow(
    context!!
) {
    override fun create() {
        super.create()
        mViewMode = WRAP_CONTENT_NOT_TOUCHABLE
        mGravity = Gravity.CENTER_VERTICAL or Gravity.START
        inflate(R.layout.main_layout_float_not_full_screen_touch_disable)
    }

    override fun onAddWindowFailed(e: Exception?) {}
}