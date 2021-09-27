package com.lishuaihua.floatwindow

import android.content.Context
import android.view.Gravity
import android.view.View
import java.lang.Exception

class FloatPermissionDeleteViewFloatWindow(context: Context?) : BaseFloatWindow(
    (context)!!
) {
    override fun create() {
        super.create()
        mViewMode = WRAP_CONTENT_TOUCHABLE
        mGravity = Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL
        inflate(R.layout.main_layout_float_permission_detect)
        findView<View>(R.id.btn_close)!!.setOnClickListener(
            View.OnClickListener { remove() })
    }

    override fun onAddWindowFailed(e: Exception?) {}
}