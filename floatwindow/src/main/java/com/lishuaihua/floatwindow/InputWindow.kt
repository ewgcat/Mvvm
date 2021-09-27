package com.lishuaihua.floatwindow

import android.content.Context
import android.view.View
import java.lang.Exception

class InputWindow(context: Context?) : BaseFloatWindow((context)!!) {
    override fun create() {
        super.create()
        mViewMode = WRAP_CONTENT_TOUCHABLE
        inflate(R.layout.main_layout_input_window)
        requestFocus(true)
        findView<View>(R.id.btn_close)!!.setOnClickListener(
            View.OnClickListener { remove() })
    }

    override fun onAddWindowFailed(e: Exception?) {}
}