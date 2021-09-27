package com.lishuaihua.floatwindow

import android.content.Context
import android.view.View
import android.widget.Toast
import java.lang.Exception

/**
 * @author sun on 2018/12/26.
 */
class FullScreenTouchAbleFloatWindowFloatWindow(context: Context?) : BaseFloatWindow(
    (context)!!
) {
    override fun create() {
        super.create()
        mViewMode = FULLSCREEN_TOUCHABLE
        inflate(R.layout.main_layout_float_full_screen_touch_able)
        findView<View>(R.id.btn_close)!!.setOnClickListener(
            View.OnClickListener {
                Toast.makeText(mContext, "remove", Toast.LENGTH_SHORT).show()
                remove()
            })
    }

    override fun onAddWindowFailed(e: Exception?) {}
}