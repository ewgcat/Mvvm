package com.lishuaihua.baselib.base

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialog
import com.lishuaihua.baselib.R
import com.lishuaihua.baselib.bus.LiveDataBus
import com.lishuaihua.baselib.sp.SharedPreferencesManager
import com.lishuaihua.baselib.widget.GrayFrameLayout


abstract class BaseActivity: AppCompatActivity() {

    @LayoutRes
    var resId: Int = 0

    @LayoutRes
    protected abstract fun getLayoutResId(): Int

    /**
     * Correspond to fragment's [Fragment.getContext]
     * @return context
     */
    protected val context: Context
        get() = this

    /**
     * Correspond to fragment's [Fragment.getActivity]
     * @return activity
     */
    protected val activity: Activity
        get() = this
    protected//获取status_bar_height资源的ID
    //根据资源ID获取响应的尺寸值
    val statusBarHeight: Int
        get() {
            var height = 0
            val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                height = resources.getDimensionPixelSize(resourceId)
            }
            return height
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resId = getLayoutResId()
        require(resId > 0) { "The subclass must provider a valid layout resources id." }
        setContentView(resId)
        doCreateView(savedInstanceState)
        LiveDataBus.get().with("UpdateGloableColor")!!.observe(this, {
            if ("UpdateGloableColor".equals(it)) {
                recreate()
            }
        })

    }


    protected abstract fun doCreateView(savedInstanceState: Bundle?)

    /**
     * 根据首选项控制整个界面颜色
     */
    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        var defaultGloableColor =
            SharedPreferencesManager.getInstance(context)!!.getInt("defaultGloableColor", 0)
        if (defaultGloableColor == 1) {
            if (("FrameLayout" == name)) {
                var count: Int = attrs.getAttributeCount()
                for (i in 0 until count) {
                    var attributeName: String? = attrs.getAttributeName(i)
                    var attributeValue: String? = attrs.getAttributeValue(i)
                    if ((attributeName == "id")) {
                        var id: Int = attributeValue?.substring(1)!!.toInt()
                        var idVal: String? = getResources().getResourceName(id)
                        if (("android:id/content" == idVal)) {
                            var grayFrameLayout: GrayFrameLayout? = GrayFrameLayout(context, attrs)
                            return grayFrameLayout
                        }
                    }
                }
            }
        } else {
            if (("FrameLayout" == name)) {
                var count: Int = attrs.getAttributeCount()
                for (i in 0 until count) {
                    var attributeName: String? = attrs.getAttributeName(i)
                    var attributeValue: String? = attrs.getAttributeValue(i)
                    if ((attributeName == "id")) {
                        var id: Int = attributeValue?.substring(1)!!.toInt()
                        var idVal: String? = getResources().getResourceName(id)
                        if (("android:id/content" == idVal)) {
                            var frameLayout: FrameLayout? = FrameLayout(context, attrs)
                            return frameLayout
                        }
                    }
                }
            }
        }
        return super.onCreateView(name, context, attrs)
    }

    /**
     * 请求动画
     */
    private var dialog: AlertDialog? = null
    protected open fun showLoading() {
        showLoading(null)
    }
    protected open fun showLoading(message: String?) {
        if (dialog == null) {
            dialog = AlertDialog.Builder(this).create()
            dialog?.window?.setBackgroundDrawable(ColorDrawable())
            dialog?.window?.setDimAmount(0F)
            dialog?.setCancelable(false)
            dialog?.setOnKeyListener { _, _, _ -> false }
            dialog?.show()
            dialog?.setContentView(R.layout.view_custom_loading)
            val tvLoadingMessage = dialog?.findViewById<TextView>(R.id.tv_loading_message)
            if (!TextUtils.isEmpty(message)){
                tvLoadingMessage?.setText(message)
            }else{
                tvLoadingMessage?.setText(R.string.loading)
            }
            dialog?.setCanceledOnTouchOutside(false)
        }
        if (!activity.isFinishing) {
            dialog?.show()
        }
    }

    protected open fun hideLoading() {
        if (!activity.isFinishing) {
            dialog?.dismiss()
        }
    }
    override fun onDestroy() {
        if (!activity.isFinishing) {
            dialog?.dismiss()
        }
        super.onDestroy()
    }


    /**
     * 触摸EditText 隐藏软键盘
     */
    var editText: EditText? = null
    open fun isShouldHideInput(v: View?, event: MotionEvent): Boolean {
        if (v != null && v is EditText) {
            editText = v
            val leftTop = intArrayOf(0, 0)
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop)
            val left = leftTop[0]
            val top = leftTop[1]
            val bottom: Int = top + v.getHeight()
            val right: Int = left + v.getWidth()
            return !(event.x > left && event.x < right && event.y > top && event.y < bottom)
        }
        return false
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev!!.action == MotionEvent.ACTION_DOWN) {
            val v: View? = currentFocus
            if (isShouldHideInput(v, ev)) { //点击editText控件外部
                val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                val tempImm = v?.context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                tempImm.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                editText?.clearFocus()
            }
            return super.dispatchTouchEvent(ev)
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return window.superDispatchTouchEvent(ev) || onTouchEvent(ev)
    }



}