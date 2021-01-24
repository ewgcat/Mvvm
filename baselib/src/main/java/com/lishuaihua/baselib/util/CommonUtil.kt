package com.lishuaihua.baselib.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.graphics.RectF
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.PopupWindow
import java.lang.reflect.Method
import java.util.*
import java.util.regex.Pattern

object CommonUtil {
    val TAG = CommonUtil::class.java.simpleName

    /**
     * 计算指定的 View 在屏幕中的坐标。
     */
    fun calcViewScreenLocation(view: View): RectF {
        val location = IntArray(2)
        // 获取控件在屏幕中的位置，返回的数组分别为控件左顶点的 x、y 的值
        view.getLocationOnScreen(location)
        return RectF(
            location[0].toFloat(), location[1]
                .toFloat(), (location[0] + view.width).toFloat(),
            (location[1] + view.height).toFloat()
        )
    }

    fun intersects(a: Rect, b: Rect): Boolean {
        var f = false
        if (a.left == b.left) {
            if (a.top > b.top && a.top < b.bottom) {
                f = true
            } else if (a.top < b.top && a.bottom > b.top) {
                f = true
            }
        } else {
            f = a.left < b.right && b.left < a.right && a.top < b.bottom && b.top < a.bottom
        }
        return f
    }

    /**
     * 判断字符串是否为整数
     */
    fun isNumeric(str: String?): Boolean {
        if (str == null) return false
        val pattern = Pattern.compile("[0-9]*")
        val isNum = pattern.matcher(str)
        return isNum.matches()
    }

    /**
     * 判断字符串是否为整数
     */
    fun isFloatNumeric(str: String?): Boolean {
        if (str == null) return false
        val pattern = Pattern.compile("(-)?[0-9]*(\\.[0-9]*)?|^-$*")
        val isNum = pattern.matcher(str)
        return isNum.matches()
    }

    /**
     * 判断字符串是否为整数
     */
    fun isColor(str: String?): Boolean {
        if (str == null) return false
        val pattern = Pattern.compile("^#([0-9a-fA-F]{6}|[0-9a-fA-F]{3})$")
        val isColor = pattern.matcher(str)
        return isColor.matches()
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dp2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dp(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * 验证日期字符串是否是YYYY-MM-DD格式
     */
    fun isDateFormat(str: String?): Boolean {
        var flag = false
        val regxStr =
            "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$"
        val pattern1 = Pattern.compile(regxStr)
        val isNo = pattern1.matcher(str)
        if (isNo.matches()) {
            flag = true
        }
        return flag
    }

    /**
     * 字符串较空，返回空字符串
     */
    fun emptyIfNull(str: String?): String {
        return str ?: ""
    }

    fun isEmpty(str: String): Boolean {
        return TextUtils.isEmpty(str) || "null" == str
    }

    /**
     * 验证密码
     */
    fun isPassWordFormat(str: String?): Boolean {
        var flag = false
        val regxStr = "^[0-9a-zA-Z_]{6,20}$"
        val pattern1 = Pattern.compile(regxStr)
        val isNo = pattern1.matcher(str)
        if (isNo.matches()) {
            flag = true
        }
        return flag
    }

    /**
     * 校验Tag Alias 只能是数字,英文字母和中文
     *
     * @param s
     * @return
     */
    fun isValidTagAndAlias(s: String?): Boolean {
        val p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_!@#$&*+=.|]+$")
        val m = p.matcher(s)
        return m.matches()
    }

    fun isMatchName(s: String?): Boolean {
        val p = Pattern.compile("^[\u4E00-\u9FA5a-zA-Z]+$")
        val m = p.matcher(s)
        return m.matches()
    }

    /**
     * 获取版本名
     *
     * @param context
     * @return
     */
    fun getVersionName(context: Context): String {
        return try {
            val manager = context.packageManager.getPackageInfo(context.packageName, 0)
            manager.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            "Unknown"
        }
    }

    /**
     * 获取版本名
     *
     * @param context
     * @return
     */
    fun getAccessStatisticsVersionName(context: Context): String {
        return try {
            val manager = context.packageManager.getPackageInfo(context.packageName, 0)
            "Android_" + manager.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            "Unknown"
        }
    }

    fun fitPopupWindowOverStatusBar(popupWindow: PopupWindow?, needFullScreen: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                val mLayoutInScreen = PopupWindow::class.java.getDeclaredField("mLayoutInScreen")
                mLayoutInScreen.isAccessible = true
                mLayoutInScreen[popupWindow] = needFullScreen
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 获取版本名
     *
     * @param context
     * @return
     */
    fun getVersionCode(context: Context): Int {
        return try {
            val manager = context.packageManager.getPackageInfo(context.packageName, 0)
            manager.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            0
        }
    }

    //用户id
    private var uuid: String? = null

    /**
     * 得到全局唯一UUID
     */
    fun getUUID(context: Context): String? {
        val mShare = context.getSharedPreferences("sysCacheMap", Context.MODE_PRIVATE)
        if (mShare != null) {
            uuid = mShare.getString("uuid", "")
        }
        if (TextUtils.isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString()
            mShare!!.edit().putString("uuid", uuid).commit()
        }
        return uuid
    }

    /**
     * 给PopWindow设置是否可以触摸
     *
     * @param popupWindow
     * @param touchModal
     */
    fun setPopupWindowTouchModal(popupWindow: PopupWindow?, touchModal: Boolean) {
        if (null == popupWindow) {
            return
        }
        val method: Method
        try {
            method = PopupWindow::class.java.getDeclaredMethod(
                "setTouchModal",
                Boolean::class.javaPrimitiveType
            )
            method.isAccessible = true
            method.invoke(popupWindow, touchModal)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun callPhone(context: Context, phone: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
        context.startActivity(intent)
    }

    fun showSoftInputFromWindow(activity: Activity, editText: EditText) {
        editText.isFocusable = true
        editText.isFocusableInTouchMode = true
        editText.requestFocus()
        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

    /**
     * 通过反射的方式获取状态栏高度
     *
     * @return
     */
    fun getStatusBarHeight(context: Context): Int {
        try {
            val c = Class.forName("com.android.internal.R\$dimen")
            val obj = c.newInstance()
            val field = c.getField("status_bar_height")
            val x = field[obj].toString().toInt()
            return context.resources.getDimensionPixelSize(x)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }
}