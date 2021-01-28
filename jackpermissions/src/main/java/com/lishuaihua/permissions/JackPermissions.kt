package com.lishuaihua.permissions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.lishuaihua.permissions.PermissionFragment.Companion.beginRequest
import com.lishuaihua.permissions.PermissionSetting.getApplicationDetailsIntent
import com.lishuaihua.permissions.PermissionSetting.getSmartPermissionIntent
import com.lishuaihua.permissions.PermissionUtils.asArrayList
import com.lishuaihua.permissions.PermissionUtils.checkPermissionManifest
import com.lishuaihua.permissions.PermissionUtils.checkTargetSdkVersion
import com.lishuaihua.permissions.PermissionUtils.getFragmentActivity
import com.lishuaihua.permissions.PermissionUtils.getManifestPermissions
import com.lishuaihua.permissions.PermissionUtils.isDebugMode
import com.lishuaihua.permissions.PermissionUtils.optimizeDeprecatedPermission

final class JackPermissions{



    /** 权限列表  */
    private var mPermissions: MutableList<String>? = null


    /**
     * 设置权限组
     */
    fun permission(vararg permissions: String): JackPermissions {
        if (mPermissions == null) {
            mPermissions = asArrayList(*permissions)
        } else {
            mPermissions!!.addAll(asArrayList(*permissions)!!)
        }
        return this
    }

    /**
     * 设置权限组
     */
    fun permission(vararg permissions: Array<String>): JackPermissions {
        if (mPermissions == null) {
            var length = 0
            for (permission in permissions) {
                length += permission.size
            }
            mPermissions = ArrayList(length)
        }
        for (group in permissions) {
            mPermissions!!.addAll(asArrayList(*group)!!)
        }
        return this
    }

    /**
     * 设置权限组
     */
    fun permission(permissions: MutableList<String>): JackPermissions {
        if (mPermissions == null) {
            mPermissions = permissions
        } else {
            mPermissions!!.addAll(permissions!!)
        }
        return this
    }

    /**
     * 请求权限
     */
    fun request(callback: OnPermission?) {
        // 如果传入 Activity 为空或者 Activity 状态非法则直接屏蔽这次权限申请
        if (mActivity == null || mActivity!!.isFinishing ||
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && mActivity!!.isDestroyed
        ) {
            return
        }

        // 必须要传入权限或者权限组才能申请权限
        require(!(mPermissions == null || mPermissions!!.isEmpty())) { "The requested permission cannot be empty" }
        if (sDebugMode == null) {
            sDebugMode = isDebugMode(mActivity!!)
        }

        // 优化所申请的权限列表
        optimizeDeprecatedPermission(mPermissions!!)
        if (sDebugMode!!) {
            // 检测所申请的权限和 targetSdk 版本是否符合要求
            checkTargetSdkVersion(mActivity!!, mPermissions!!)
            // 检测权限有没有在清单文件中注册
            checkPermissionManifest(mActivity!!, mPermissions)
        }
        if (isGrantedPermission(mActivity!!, mPermissions!!)) {
            // 证明这些权限已经全部授予过，直接回调成功
            callback?.onGranted(mPermissions, true)
            return
        }

        // 申请没有授予过的权限
        beginRequest(mActivity!!, ArrayList(mPermissions), callback)
    }



    /**
     * 判断一个或多个权限是否全部授予了
     */
    fun isGrantedPermission(context: Context, permissions: List<String>?): Boolean {
        return PermissionUtils.isGrantedPermission(context, permissions)
    }


    /** 权限设置页跳转请求码  */
     val REQUEST_CODE = 1024

    /** 调试模式  */
    private var sDebugMode: Boolean? = null



    /**
     * 设置是否为调试模式
     */
    fun setDebugMode(debug: Boolean) {
        sDebugMode = debug
    }

    /**
     * 判断一个或多个权限是否全部授予了
     */
    fun isGrantedPermission(context: Context, vararg permissions: String): Boolean {
        return if (permissions.isEmpty()) {
            isGrantedPermission(
                context, getManifestPermissions(context)
            )
        } else {
            isGrantedPermission(context, asArrayList(*permissions))
        }
    }

    fun isGrantedPermission(context: Context, permissions: ArrayList<String>?): Boolean {
        return PermissionUtils.isGrantedPermission(context, permissions!!)
    }


    /**
     * 跳转到应用详情页
     */
    fun startApplicationDetails(context: Context) {
        val activity: Activity? = getFragmentActivity(context)
        if (activity != null) {
            startApplicationDetails(activity)
            return
        }
        val intent = getApplicationDetailsIntent(context)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun startApplicationDetails(activity: Activity) {
        activity.startActivityForResult(getApplicationDetailsIntent(activity), REQUEST_CODE)
    }

    fun startApplicationDetails(fragment: Fragment) {
        val activity = fragment.activity ?: return
        fragment.startActivityForResult(getApplicationDetailsIntent(activity), REQUEST_CODE)
    }

    /**
     * 跳转到应用权限设置页
     *
     * @param permissions           没有授予或者被拒绝的权限组
     */
    fun startPermissionActivity(context: Context, vararg permissions: String) {

        startPermissionActivity(context, asArrayList(*permissions))
    }

    fun startPermissionActivity(context: Context, permissions: ArrayList<String>?) {
        val activity: Activity? = getFragmentActivity(context)
        if (activity != null) {
            startPermissionActivity(activity, permissions)
            return
        }
        val intent = getSmartPermissionIntent(context, permissions)
        intent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun startPermissionActivity(activity: Activity, vararg permissions: String) {
        startPermissionActivity(activity, asArrayList(*permissions))
    }

    fun startPermissionActivity(activity: Activity, permissions: ArrayList<String>) {
        activity.startActivityForResult(
            getSmartPermissionIntent(activity, permissions),
            REQUEST_CODE
        )
    }

    fun startPermissionActivity(fragment: Fragment, vararg permissions: String) {
        startPermissionActivity(fragment, asArrayList(*permissions))
    }

    fun startPermissionActivity(fragment: Fragment, permissions: ArrayList<String>?) {
        val activity = fragment.activity ?: return
        fragment.startActivityForResult(
            getSmartPermissionIntent(activity, permissions),
            REQUEST_CODE
        )
    }

    companion object {
        @get:Synchronized
        lateinit   var mActivity: FragmentActivity
        /**
         * 私有化构造函数
         */
        private fun JackPermissions(activity: FragmentActivity) {
            mActivity = activity
        }
        /**
         * 设置请求的对象
         *
         * @param activity          当前 Activity，也可以传入栈顶的 Activity
         */
        fun with(activity: FragmentActivity): JackPermissions {
            this.mActivity=activity
            return JackPermissions()
        }

        fun with(context: Context): JackPermissions {
            this.mActivity=getFragmentActivity(context)!!
            return with(mActivity)
        }

        fun with(fragment: Fragment): JackPermissions {
            this.mActivity= fragment.requireActivity()
            return with(mActivity)
        }
    }
}

