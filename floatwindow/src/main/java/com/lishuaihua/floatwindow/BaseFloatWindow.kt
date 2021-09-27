package com.lishuaihua.floatwindow

import android.content.Context
import android.os.Handler
import android.view.WindowManager
import android.view.Gravity
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import java.lang.Exception

abstract class BaseFloatWindow(var mContext: Context) {
    @JvmField
    var mLayoutParams: WindowManager.LayoutParams? = null
    @JvmField
    var mInflate: View? = null
    @JvmField
    var mWindowManager: WindowManager? = null
    private var mAdded = false

    //设置隐藏时是否是INVISIBLE
    private var mInvisibleNeed = false
    private var mRequestFocus = false
    @JvmField
    var mGravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL
    @JvmField
    var mViewMode = WRAP_CONTENT_NOT_TOUCHABLE
    var mHandler: Handler? = Handler(Looper.getMainLooper())
    @JvmField
    protected var mAddX = 0
    @JvmField
    protected var mAddY = 0

    /**
     * 设置隐藏View的方式是否为Invisible，默认为Gone
     *
     * @param invisibleNeed 是否是Invisible
     */
    fun setInvisibleNeed(invisibleNeed: Boolean) {
        mInvisibleNeed = invisibleNeed
    }

    /**
     * 悬浮窗是否需要获取焦点，通常获取焦点后，悬浮窗可以和软键盘发生交互，被覆盖的应用失去焦点。
     * 例如：游戏将失去背景音乐
     *
     * @param requestFocus
     */
    fun requestFocus(requestFocus: Boolean) {
        mRequestFocus = requestFocus
    }

    @CallSuper
    open fun create() {
        mWindowManager =
            mContext.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    @CallSuper
    @Synchronized
    fun show() {
        checkNotNull(mInflate) { "FloatView can not be null" }
        if (mAdded) {
            mInflate!!.visibility = View.VISIBLE
            return
        }
        getLayoutParam(mViewMode)
        mInflate!!.visibility = View.VISIBLE
        try {
            mLayoutParams!!.x = mAddX
            mLayoutParams!!.y = mAddY
            mWindowManager!!.addView(mInflate, mLayoutParams)
            mAdded = true
        } catch (e: Exception) {
            Log.e(TAG, "添加悬浮窗失败！！！！！！请检查悬浮窗权限")
            //            Toast.makeText(mContext, "添加悬浮窗失败！！！！！！请检查悬浮窗权限", Toast.LENGTH_SHORT).show();
            onAddWindowFailed(e)
        }
    }

    @CallSuper
    fun hide() {
        if (mInflate != null) {
            mInflate!!.visibility = View.INVISIBLE
        }
    }

    @CallSuper
    fun gone() {
        if (mInflate != null) {
            mInflate!!.visibility = View.GONE
        }
    }

    @CallSuper
    fun remove() {
        if (mInflate != null && mWindowManager != null) {
            if (mInflate!!.isAttachedToWindow) {
                mWindowManager!!.removeView(mInflate)
            }
            mAdded = false
        }
        if (mHandler != null) {
            mHandler!!.removeCallbacksAndMessages(null)
        }
    }

    @CallSuper
    protected fun inflate(@LayoutRes layout: Int): View? {
        mInflate = View.inflate(mContext, layout, null)
        return mInflate
    }

    protected abstract fun onAddWindowFailed(e: Exception?)
    protected fun <T : View?> findView(@IdRes id: Int): T? {
        return if (mInflate != null) {
            mInflate!!.findViewById<View>(id) as T
        } else null
    }

    /**
     * 获取悬浮窗LayoutParam
     *
     * @param mode
     */
    protected fun getLayoutParam(mode: Int) {
        when (mode) {
            FULLSCREEN_TOUCHABLE -> mLayoutParams =
                FloatWindowParamManager.getFloatLayoutParam(true, true)
            FULLSCREEN_NOT_TOUCHABLE -> mLayoutParams =
                FloatWindowParamManager.getFloatLayoutParam(true, false)
            WRAP_CONTENT_NOT_TOUCHABLE -> mLayoutParams =
                FloatWindowParamManager.getFloatLayoutParam(false, false)
            WRAP_CONTENT_TOUCHABLE -> mLayoutParams =
                FloatWindowParamManager.getFloatLayoutParam(false, true)
        }
        if (mRequestFocus) {
            mLayoutParams!!.flags =
                mLayoutParams!!.flags and WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE.inv()
        }
        mLayoutParams!!.gravity = mGravity
    }

    /**
     * 获取可见性
     *
     * @return
     */
    val visibility: Boolean
        get() = if (mInflate != null && mInflate!!.visibility == View.VISIBLE) {
            true
        } else {
            false
        }

    /**
     * 改变可见性
     */
    fun toggleVisibility() {
        if (mInflate != null) {
            if (visibility) {
                if (mInvisibleNeed) {
                    hide()
                } else {
                    gone()
                }
            } else {
                show()
            }
        }
    }

    companion object {
        const val TAG = "BaseFloatWindow"
        const val FULLSCREEN_TOUCHABLE = 1
        const val FULLSCREEN_NOT_TOUCHABLE = 2
        const val WRAP_CONTENT_TOUCHABLE = 3
        const val WRAP_CONTENT_NOT_TOUCHABLE = 4
    }

    init {
        create()
    }
}