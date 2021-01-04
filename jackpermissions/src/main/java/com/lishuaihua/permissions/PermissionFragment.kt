package com.lishuaihua.permissions

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.SparseBooleanArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.lishuaihua.permissions.PermissionSetting.getInstallPermissionIntent
import com.lishuaihua.permissions.PermissionSetting.getNotifyPermissionIntent
import com.lishuaihua.permissions.PermissionSetting.getSettingPermissionIntent
import com.lishuaihua.permissions.PermissionSetting.getStoragePermissionIntent
import com.lishuaihua.permissions.PermissionSetting.getWindowPermissionIntent
import com.lishuaihua.permissions.PermissionUtils.containsSpecialPermission
import com.lishuaihua.permissions.PermissionUtils.getDeniedPermissions
import com.lishuaihua.permissions.PermissionUtils.getGrantedPermissions
import com.lishuaihua.permissions.PermissionUtils.getPermissionStatus
import com.lishuaihua.permissions.PermissionUtils.isAndroid10
import com.lishuaihua.permissions.PermissionUtils.isAndroid11
import com.lishuaihua.permissions.PermissionUtils.isAndroid8
import com.lishuaihua.permissions.PermissionUtils.isAndroid9
import com.lishuaihua.permissions.PermissionUtils.isGrantedInstallPermission
import com.lishuaihua.permissions.PermissionUtils.isGrantedNotifyPermission
import com.lishuaihua.permissions.PermissionUtils.isGrantedPermission
import com.lishuaihua.permissions.PermissionUtils.isGrantedSettingPermission
import com.lishuaihua.permissions.PermissionUtils.isGrantedStoragePermission
import com.lishuaihua.permissions.PermissionUtils.isGrantedWindowPermission
import com.lishuaihua.permissions.PermissionUtils.isSpecialPermission
import com.lishuaihua.permissions.PermissionUtils.randomRequestCode
import java.util.*

class PermissionFragment : Fragment() {
    /** 是否申请了特殊权限  */
    private var mSpecialRequest = false

    /** 是否申请了危险权限  */
    private var mDangerousRequest = false

    /** 权限回调对象  */
    private var mCallBack: OnPermission? = null

    /**
     * 设置权限监听回调监听
     */
    fun setCallBack(callback: OnPermission?) {
        mCallBack = callback
    }

    override fun onDestroy() {
        super.onDestroy()
        // 取消引用监听器，避免内存泄漏
        mCallBack = null
    }

    override fun onResume() {
        super.onResume()
        // 如果在 Activity 不可见的状态下添加 Fragment 并且去申请权限会导致授权对话框显示不出来
        // 所以必须要在 Fragment 的 onResume 来申请权限，这样就可以保证应用回到前台的时候才去申请权限
        if (mSpecialRequest) {
            return
        }
        mSpecialRequest = true
        if (mCallBack == null) {
            removeFragment(fragmentManager, this)
            return
        }
        requestSpecialPermission()
    }

    /**
     * 申请危险权限
     */
    fun requestDangerousPermission() {
        val arguments = arguments ?: return
        val allPermissions = arguments.getStringArrayList(PERMISSION_GROUP)
        if (allPermissions == null || allPermissions.size == 0) {
            return
        }
        var locationPermission: ArrayList<String?>? = null
        // Android 10 定位策略发生改变，申请后台定位权限的前提是要有前台定位权限（授予了精确或者模糊任一权限）
        if (isAndroid10 && allPermissions.contains(Permission.ACCESS_BACKGROUND_LOCATION)) {
            locationPermission = ArrayList()
            if (allPermissions.contains(Permission.ACCESS_COARSE_LOCATION) &&
                !isGrantedPermission(activity!!, Permission.ACCESS_COARSE_LOCATION)
            ) {
                locationPermission.add(Permission.ACCESS_COARSE_LOCATION)
            }
            if (allPermissions.contains(Permission.ACCESS_FINE_LOCATION) &&
                !isGrantedPermission(activity!!, Permission.ACCESS_FINE_LOCATION)
            ) {
                locationPermission.add(Permission.ACCESS_FINE_LOCATION)
            }
        }

        // 如果不需要申请前台定位权限就直接申请危险权限
        if (locationPermission == null || locationPermission.isEmpty()) {
            requestPermissions(allPermissions.toTypedArray(), getArguments()!!.getInt(REQUEST_CODE))
            return
        }
        val activity = activity ?: return

        // 在 Android 10 的机型上，需要先申请前台定位权限，再申请后台定位权限
        beginRequest(activity, locationPermission, object : OnPermission {
            override fun onGranted(permissions: List<String>?, all: Boolean) {
                if (!all || !isAdded) {
                    return
                }
                val arguments = getArguments() ?: return
                requestPermissions(allPermissions.toTypedArray(), arguments.getInt(REQUEST_CODE))
            }

            override fun onDenied(permissions: List<String>?, never: Boolean) {
                if (!isAdded) {
                    return
                }
                val arguments = getArguments() ?: return
                requestPermissions(allPermissions.toTypedArray(), arguments.getInt(REQUEST_CODE))
            }
        })
    }

    /**
     * 申请特殊权限
     */
    fun requestSpecialPermission() {
        val arguments = arguments ?: return
        val permissions: List<String>? = arguments.getStringArrayList(PERMISSION_GROUP)

        // 是否需要申请特殊权限
        var requestSpecialPermission = false

        // 判断当前是否包含特殊权限
        if (containsSpecialPermission(permissions)) {
            if (permissions!!.contains(Permission.MANAGE_EXTERNAL_STORAGE) && !isGrantedStoragePermission(
                    activity
                )
            ) {
                // 当前必须是 Android 11 及以上版本，因为 hasStoragePermission 在旧版本上是拿旧权限做的判断，所以这里需要多判断一次版本
                if (isAndroid11) {
                    // 跳转到存储权限设置界面
                    startActivityForResult(
                        getStoragePermissionIntent(activity!!), getArguments()!!.getInt(
                            REQUEST_CODE
                        )
                    )
                    requestSpecialPermission = true
                }
            }
            if (permissions.contains(Permission.REQUEST_INSTALL_PACKAGES) && !isGrantedInstallPermission(
                    activity!!
                )
            ) {
                // 跳转到安装权限设置界面
                startActivityForResult(
                    getInstallPermissionIntent(activity!!), getArguments()!!.getInt(
                        REQUEST_CODE
                    )
                )
                requestSpecialPermission = true
            }
            if (permissions.contains(Permission.SYSTEM_ALERT_WINDOW) && !isGrantedWindowPermission(
                    activity
                )
            ) {
                // 跳转到悬浮窗设置页面
                startActivityForResult(
                    getWindowPermissionIntent(activity!!), getArguments()!!.getInt(
                        REQUEST_CODE
                    )
                )
                requestSpecialPermission = true
            }
            if (permissions.contains(Permission.NOTIFICATION_SERVICE) && !isGrantedNotifyPermission(
                    activity!!
                )
            ) {
                // 跳转到通知栏权限设置页面
                startActivityForResult(
                    getNotifyPermissionIntent(activity!!), getArguments()!!.getInt(
                        REQUEST_CODE
                    )
                )
                requestSpecialPermission = true
            }
            if (permissions.contains(Permission.WRITE_SETTINGS) && !isGrantedSettingPermission(
                    activity
                )
            ) {
                // 跳转到系统设置权限设置页面
                startActivityForResult(
                    getSettingPermissionIntent(activity!!), getArguments()!!.getInt(
                        REQUEST_CODE
                    )
                )
                requestSpecialPermission = true
            }
        }

        // 当前必须没有跳转到悬浮窗或者安装权限界面
        if (!requestSpecialPermission) {
            requestDangerousPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        val arguments = arguments ?: return
        if (requestCode != arguments.getInt(REQUEST_CODE)) {
            return
        }
        val callBack = mCallBack
        mCallBack = null

        // 根据请求码取出的对象为空，就直接返回不处理
        if (callBack == null) {
            return
        }
        for (i in permissions.indices) {
            val permission = permissions[i]
            if (isSpecialPermission(permission)) {
                // 如果这个权限是特殊权限，那么就重新进行权限检测
                grantResults[i] = getPermissionStatus(activity!!, permission)
                continue
            }
            if (isAndroid11 && Permission.ACCESS_BACKGROUND_LOCATION == permission) {
                // 这个权限是后台定位权限并且当前手机版本是 Android 11 及以上，那么就需要重新进行检测
                // 因为只要申请这个后台定位权限，grantResults 数组总对这个权限申请的结果返回 -1（拒绝）
                grantResults[i] = getPermissionStatus(requireActivity(), permission)
                continue
            }
            if (!isAndroid10) {
                // 重新检查 Android 10.0 的三个新权限
                if (Permission.ACCESS_BACKGROUND_LOCATION == permission || Permission.ACTIVITY_RECOGNITION == permission || Permission.ACCESS_MEDIA_LOCATION == permission) {
                    // 如果当前版本不符合最低要求，那么就重新进行权限检测
                    grantResults[i] = getPermissionStatus(activity!!, permission)
                    continue
                }
            }
            if (!isAndroid9) {
                // 重新检查 Android 9.0 的一个新权限
                if (Permission.ACCEPT_HANDOVER == permission) {
                    // 如果当前版本不符合最低要求，那么就重新进行权限检测
                    grantResults[i] = getPermissionStatus(activity!!, permission)
                }
            }
            if (!isAndroid8) {
                // 重新检查 Android 8.0 的两个新权限
                if (Permission.ANSWER_PHONE_CALLS == permission || Permission.READ_PHONE_NUMBERS == permission) {
                    // 如果当前版本不符合最低要求，那么就重新进行权限检测
                    grantResults[i] = getPermissionStatus(activity!!, permission)
                }
            }
        }

        // 释放对这个请求码的占用
        sRequestCodes.delete(requestCode)
        // 将 Fragment 从 Activity 移除
        removeFragment(fragmentManager, this)

        // 获取已授予的权限
        val grantedPermission: List<String> = getGrantedPermissions(permissions, grantResults)

        // 如果请求成功的权限集合大小和请求的数组一样大时证明权限已经全部授予
        if (grantedPermission.size == permissions.size) {
            // 代表申请的所有的权限都授予了
            callBack.onGranted(grantedPermission, true)
            return
        }

        // 获取被拒绝的权限
        val deniedPermission: List<String> = getDeniedPermissions(permissions, grantResults)

        // 代表申请的权限中有不同意授予的，如果有某个权限被永久拒绝就返回 true 给开发人员，让开发者引导用户去设置界面开启权限
        callBack.onDenied(
            deniedPermission,
            PermissionUtils.isPermissionPermissionDenied(requireActivity(), deniedPermission)
        )

        // 证明还有一部分权限被成功授予，回调成功接口
        if (!grantedPermission.isEmpty()) {
            callBack.onGranted(grantedPermission, false)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        val arguments = arguments ?: return
        if (!mDangerousRequest && requestCode == arguments.getInt(REQUEST_CODE)) {
            mDangerousRequest = true
            // 需要延迟执行，不然有些华为机型授权了但是获取不到权限
            Handler(Looper.getMainLooper()).postDelayed(Runnable { // 如果用户离开太久，会导致 Activity 被回收掉
                // 所以这里要判断当前 Fragment 是否有被添加到 Activity
                // 可在开发者模式中开启不保留活动来复现这个 Bug
                if (!isAdded) {
                    return@Runnable
                }
                // 请求其他危险权限
                requestDangerousPermission()
            }, 300)
        }
    }

    companion object {
        /** 请求的权限组  */
        private const val PERMISSION_GROUP = "permission_group"

        /** 请求码（自动生成）  */
        private const val REQUEST_CODE = "request_code"

        /** 权限请求码存放集合  */
        private val sRequestCodes = SparseBooleanArray()

        /**
         * 开启权限申请
         */
        @JvmStatic
        fun beginRequest(
            activity: FragmentActivity,
            permissions: ArrayList<String?>?,
            callback: OnPermission?
        ) {
            val fragment = PermissionFragment()
            val bundle = Bundle()
            var requestCode: Int
            // 请求码随机生成，避免随机产生之前的请求码，必须进行循环判断
            do {
                requestCode = randomRequestCode
            } while (sRequestCodes[requestCode])
            // 标记这个请求码已经被占用
            sRequestCodes.put(requestCode, true)
            bundle.putInt(REQUEST_CODE, requestCode)
            bundle.putStringArrayList(PERMISSION_GROUP, permissions)
            fragment.arguments = bundle
            // 设置保留实例，不会因为配置变化而重新创建
            fragment.retainInstance = true
            // 设置权限回调监听
            fragment.setCallBack(callback)
            addFragment(activity.supportFragmentManager, fragment)
        }

        /**
         * 添加 Fragment
         */
        fun addFragment(manager: FragmentManager?, fragment: Fragment) {
            if (manager == null) {
                return
            }
            manager.beginTransaction().add(fragment, fragment.toString()).commitAllowingStateLoss()
        }

        /**
         * 移除 Fragment
         */
        fun removeFragment(manager: FragmentManager?, fragment: Fragment?) {
            if (manager == null) {
                return
            }
            manager.beginTransaction().remove(fragment!!).commitAllowingStateLoss()
        }
    }
}