package com.lishuaihua.floatwindow

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.Gravity
import com.blankj.utilcode.util.SizeUtils
import android.view.ViewConfiguration
import android.view.View.OnTouchListener
import android.view.MotionEvent
import android.view.View
import java.lang.Exception

class FollowTouchView(context: Context?) : BaseFloatWindow(context!!) {
    private val mScaledTouchSlop: Int
    private fun jumpHome() {
        val intent = Intent()
        intent.action = Intent.ACTION_MAIN
        intent.addCategory(Intent.CATEGORY_HOME)
        mContext.startActivity(intent)
    }

    override fun onAddWindowFailed(e: Exception?) {}

    init {
        mViewMode = WRAP_CONTENT_TOUCHABLE
        mGravity = Gravity.START or Gravity.TOP
        mAddX = SizeUtils.dp2px(100f)
        mAddY = SizeUtils.dp2px(100f)
        inflate(R.layout.main_layout_follow_touch)
        mScaledTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        mInflate!!.setOnTouchListener(object : OnTouchListener {
            private var mLastY = 0f
            private var mLastX = 0f
            private var mDownY = 0f
            private var mDownX = 0f
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                val x = event.rawX
                val y = event.rawY
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        mDownX = x
                        mDownY = y
                        mLastX = x
                        mLastY = y
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val moveX = x - mLastX
                        val moveY = y - mLastY
                        Log.e("TAG", "$moveX $moveY")
                        mLayoutParams!!.x += moveX.toInt()
                        mLayoutParams!!.y += moveY.toInt()
                        mWindowManager!!.updateViewLayout(mInflate, mLayoutParams)
                        mLastX = x
                        mLastY = y
                    }
                    MotionEvent.ACTION_UP -> {
                        val disX = x - mDownX
                        val disY = y - mDownY
                        val sqrt = Math.sqrt(
                            Math.pow(disX.toDouble(), 2.0) + Math.pow(
                                disY.toDouble(),
                                2.0
                            )
                        )
                        if (sqrt < mScaledTouchSlop) {
                            jumpHome()
                        }
                    }
                }
                return false
            }
        })
    }
}