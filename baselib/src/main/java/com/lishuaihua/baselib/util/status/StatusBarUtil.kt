package com.lishuaihua.baselib.util.status

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils

object StatusBarUtil {
    private fun setStatusBarColor(activity: Activity, statusColor: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarLollipop.setStatusBarColor(activity, statusColor)
        }
    }

    @JvmOverloads
    fun translucentStatusBar(activity: Activity, hideStatusBarBackground: Boolean = false) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarLollipop.translucentStatusBar(activity, hideStatusBarBackground)
        }
    }

    /**
     * 设置状态栏颜色
     * 状态栏字体颜色 font color
     * @param activity
     * @param color
     */
    fun setStatusBarLightMode(activity: Activity, color: Int) {
        setStatusBarColor(activity, color)
        lintBuildStatusBarMode(activity, color)
    }

    /**
     * 检测 Android sdk 版本 设置 状态栏字体显示颜色
     *
     * @param activity
     * @param color
     */
    private fun lintBuildStatusBarMode(activity: Activity, color: Int) {
        if (isLightColor(color)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        } else {
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
    }

    /**
     * 判断颜色是不是亮色
     *
     * @param color
     * @return
     * @from
     */
    private fun isLightColor(@ColorInt color: Int): Boolean {
        return ColorUtils.calculateLuminance(color) >= 0.5
    }

    fun setContentTopPadding(activity: Activity, padding: Int) {
        val mContentView = activity.window.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
        mContentView.setPadding(0, padding, 0, 0)
    }

    fun getPxFromDp(context: Context, dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
}