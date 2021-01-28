package com.lishuaihua.baselib.util.status

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.ColorUtils
import androidx.core.view.ViewCompat
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.lishuaihua.baselib.util.status.StatusBarUtil.setStatusBarLightMode

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
internal object StatusBarLollipop {
    /**
     * 设置状态栏的颜色
     * 1.取消状态栏透明
     * 2.设置状态栏颜色
     * 3.view 不根据系统窗口调整自己的布局
     *
     * @param activity
     * @param statusColor
     */
    fun setStatusBarColor(activity: Activity, statusColor: Int) {
        val window = activity.window
        //取消状态栏透明
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        //设置状态栏颜色
        window.statusBarColor = statusColor
        //设置系统状态栏处于可见状态
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        //让view不根据系统窗口来调整自己的布局
        val mContentView = window.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
        val mChildView = mContentView.getChildAt(0)
        if (mChildView != null) {
            mChildView.fitsSystemWindows = false
            ViewCompat.requestApplyInsets(mChildView)
        }
    }

    /**
     * 透明状态栏
     * 1.取消状态栏透明
     * 2.设置状态栏颜色
     * 3.view 不根据系统窗口调整自己的布局
     *
     * @param activity
     * @param hideStatusBarBackground
     */
    fun translucentStatusBar(activity: Activity, hideStatusBarBackground: Boolean) {
        val window = activity.window
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (hideStatusBarBackground) {
            //如果为全透明模式，取消设置Window半透明的Flag
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //设置状态栏为透明
            window.statusBarColor = Color.TRANSPARENT
            //设置window的状态栏不可见
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        } else {
            //如果为半透明模式，添加设置Window半透明的Flag
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //设置系统状态栏处于可见状态
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
        //view不根据系统窗口来调整自己的布局
        val mContentView = window.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
        val mChildView = mContentView.getChildAt(0)
        if (mChildView != null) {
            mChildView.fitsSystemWindows = false
            ViewCompat.requestApplyInsets(mChildView)
        }
    }

    fun setStatusBarColorForCollapsingToolbar(
        activity: Activity,
        appBarLayout: AppBarLayout,
        collapsingToolbarLayout: CollapsingToolbarLayout,
        toolbar: Toolbar,
        statusColor: Int
    ) {
        val window = activity.window
        //取消设置Window半透明的Flag
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        ////添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        //设置状态栏为透明
        window.statusBarColor = Color.TRANSPARENT
        //设置状态栏为不可见状态
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        //通过OnApplyWindowInsetsListener()使Layout在绘制过程中将View向下偏移了,使collapsingToolbarLayout可以占据状态栏
        ViewCompat.setOnApplyWindowInsetsListener(collapsingToolbarLayout) { v, insets -> insets }
        val mContentView = window.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
        val mChildView = mContentView.getChildAt(0)
        //view不根据系统窗口来调整自己的布局
        if (mChildView != null) {
            mChildView.fitsSystemWindows = false
            ViewCompat.requestApplyInsets(mChildView)
        }
        (appBarLayout.parent as View).fitsSystemWindows = false
        appBarLayout.fitsSystemWindows = false
        collapsingToolbarLayout.fitsSystemWindows = false
        collapsingToolbarLayout.getChildAt(0).fitsSystemWindows = false
        //设置状态栏的颜色
        collapsingToolbarLayout.setStatusBarScrimColor(statusColor)
        toolbar.fitsSystemWindows = false
        //为Toolbar添加一个状态栏的高度, 同时为Toolbar添加paddingTop,使Toolbar覆盖状态栏，ToolBar的title可以正常显示.
        val lp = toolbar.layoutParams as CollapsingToolbarLayout.LayoutParams
        val statusBarHeight = getStatusBarHeight(activity)
        lp.height += statusBarHeight
        toolbar.layoutParams = lp
        toolbar.setPadding(
            toolbar.paddingLeft,
            toolbar.paddingTop + statusBarHeight,
            toolbar.paddingRight,
            toolbar.paddingBottom
        )
        appBarLayout.addOnOffsetChangedListener(object : OnOffsetChangedListener {

            val EXPANDED = 0
            val COLLAPSED = 1
            private var appBarLayoutState = 0
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                //toolbar被折叠时显示状态栏
                if (Math.abs(verticalOffset) > collapsingToolbarLayout.scrimVisibleHeightTrigger) {
                    if (appBarLayoutState != COLLAPSED) {
                        appBarLayoutState = COLLAPSED //修改状态标记为折叠
                        val lp = toolbar.layoutParams as CollapsingToolbarLayout.LayoutParams
                        val statusBarHeight = getStatusBarHeight(activity)
                        lp.height -= statusBarHeight
                        toolbar.layoutParams = lp
                        toolbar.setPadding(0, 0, 0, 0)
                        if (statusColor == Color.WHITE) {
                            if (MIUISetStatusBarLightMode(
                                    activity,
                                    true
                                ) || FlymeSetStatusBarLightMode(activity, true)
                            ) {
                                //设置状态栏为指定颜色
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //5.0
                                    activity.window.statusBarColor = statusColor
                                    val mContentView = activity.window.findViewById<ViewGroup>(
                                        Window.ID_ANDROID_CONTENT
                                    )
                                    val mChildView = mContentView.getChildAt(0)
                                    if (mChildView != null) {
                                        mChildView.fitsSystemWindows = false
                                        ViewCompat.requestApplyInsets(mChildView)
                                    }
                                }
                            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                //如果是6.0以上将状态栏文字改为黑色，并设置状态栏颜色
                                activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                                activity.window.statusBarColor = statusColor
                                // 如果亮色，设置状态栏文字为黑色
                                if (isLightColor(statusColor)) {
                                    activity.window.decorView.systemUiVisibility =
                                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                                } else {
                                    activity.window.decorView.systemUiVisibility =
                                        View.SYSTEM_UI_FLAG_VISIBLE
                                }
                                val mContentView =
                                    activity.window.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
                                val mChildView = mContentView.getChildAt(0)
                                if (mChildView != null) {
                                    mChildView.fitsSystemWindows = false
                                    ViewCompat.requestApplyInsets(mChildView)
                                }
                            } else {
                                setStatusBarColor(activity, statusColor)
                            }
                        } else {
                            setStatusBarColor(activity, statusColor)
                        }
                    }
                } else {
                    if (appBarLayoutState != EXPANDED) {
                        //toolbar显示时同时显示状态栏
                        appBarLayoutState = EXPANDED //修改状态标记为展开
                        val lp = toolbar.layoutParams as CollapsingToolbarLayout.LayoutParams
                        val statusBarHeight = getStatusBarHeight(activity)
                        lp.height += statusBarHeight
                        toolbar.layoutParams = lp
                        toolbar.setPadding(
                            toolbar.paddingLeft, toolbar.paddingTop + statusBarHeight,
                            toolbar.paddingRight, toolbar.paddingBottom
                        )
                        translucentStatusBar(activity, true)
                    }
                }
            }
        })
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

    /**
     * MIUI的沉浸支持透明白色字体和透明黑色字体
     * https://dev.mi.com/console/doc/detail?pId=1159
     */
    fun MIUISetStatusBarLightMode(activity: Activity, darkmode: Boolean): Boolean {
        try {
            val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
            val window = activity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            val clazz: Class<out Window> = activity.window.javaClass
            val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
            val darkModeFlag = field.getInt(layoutParams)
            val extraFlagField = clazz.getMethod(
                "setExtraFlags",
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType
            )
            extraFlagField.invoke(activity.window, if (darkmode) darkModeFlag else 0, darkModeFlag)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格，Flyme4.0以上
     */
    fun FlymeSetStatusBarLightMode(activity: Activity, darkmode: Boolean): Boolean {
        try {
            val lp = activity.window.attributes
            val darkFlag = WindowManager.LayoutParams::class.java
                .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
            val meizuFlags = WindowManager.LayoutParams::class.java
                .getDeclaredField("meizuFlags")
            darkFlag.isAccessible = true
            meizuFlags.isAccessible = true
            val bit = darkFlag.getInt(null)
            var value = meizuFlags.getInt(lp)
            value = if (darkmode) {
                value or bit
            } else {
                value and bit.inv()
            }
            meizuFlags.setInt(lp, value)
            activity.window.attributes = lp
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun setStatusBarWhiteForCollapsingToolbar(
        activity: Activity, appBarLayout: AppBarLayout,
        collapsingToolbarLayout: CollapsingToolbarLayout, toolbar: Toolbar, statusBarColor: Int
    ) {
        val window = activity.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        ViewCompat.setOnApplyWindowInsetsListener(collapsingToolbarLayout) { v, insets -> insets }
        val mContentView = window.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
        val mChildView = mContentView.getChildAt(0)
        if (mChildView != null) {
            mChildView.fitsSystemWindows = false
            ViewCompat.requestApplyInsets(mChildView)
        }
        (appBarLayout.parent as View).fitsSystemWindows = false
        appBarLayout.fitsSystemWindows = false
        toolbar.fitsSystemWindows = false
        if (toolbar.tag == null) {
            val lp = toolbar.layoutParams as CollapsingToolbarLayout.LayoutParams
            val statusBarHeight = getStatusBarHeight(activity)
            lp.height += statusBarHeight
            toolbar.layoutParams = lp
            toolbar.setPadding(
                toolbar.paddingLeft,
                toolbar.paddingTop + statusBarHeight,
                toolbar.paddingRight,
                toolbar.paddingBottom
            )
            toolbar.tag = true
        }
        collapsingToolbarLayout.fitsSystemWindows = false
        collapsingToolbarLayout.getChildAt(0).fitsSystemWindows = false
        collapsingToolbarLayout.setStatusBarScrimColor(statusBarColor)
        appBarLayout.addOnOffsetChangedListener(object : OnOffsetChangedListener {

            val EXPANDED = 0
            val COLLAPSED = 1
            private var appBarLayoutState = 0
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                //toolbar被折叠时显示状态栏
                if (Math.abs(verticalOffset) > collapsingToolbarLayout.scrimVisibleHeightTrigger) {
                    if (appBarLayoutState != COLLAPSED) {
                        appBarLayoutState = COLLAPSED //修改状态标记为折叠
                        setStatusBarLightMode(activity, statusBarColor)
                    }
                } else {
                    //toolbar显示时同时显示状态栏
                    if (appBarLayoutState != EXPANDED) {
                        appBarLayoutState = EXPANDED //修改状态标记为展开
                        translucentStatusBar(activity, true)
                    }
                }
            }
        })
    }

    private fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resId > 0) {
            result = context.resources.getDimensionPixelOffset(resId)
        }
        return result
    }
}