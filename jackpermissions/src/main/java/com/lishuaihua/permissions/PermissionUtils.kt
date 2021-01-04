package com.lishuaihua.permissions

import android.app.Activity
import android.app.AppOpsManager
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.fragment.app.FragmentActivity
import com.lishuaihua.permissions.PermissionUtils.isGrantedPermission
import java.lang.reflect.InvocationTargetException
import java.util.*

object PermissionUtils {
    /**
     * 是否是 Android 11 及以上版本
     */
    val isAndroid11: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R

    /**
     * 是否是 Android 10 及以上版本
     */
    val isAndroid10: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    /**
     * 是否是 Android 9.0 及以上版本
     */
    val isAndroid9: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

    /**
     * 是否是 Android 8.0 及以上版本
     */
    val isAndroid8: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

    /**
     * 是否是 Android 7.0 及以上版本
     */
    val isAndroid7: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

    /**
     * 是否是 Android 6.0 及以上版本
     */
    val isAndroid6: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

    /**
     * 返回应用程序在清单文件中注册的权限
     */
    @JvmStatic
    fun getManifestPermissions(context: Context): List<String>? {
        return try {
            val requestedPermissions = context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.GET_PERMISSIONS
            ).requestedPermissions
            // 当清单文件没有注册任何权限的时候，那么这个数组对象就是空的
            if (requestedPermissions != null) {
                asArrayList(*requestedPermissions)
            } else {
                null
            }
        } catch (ignored: PackageManager.NameNotFoundException) {
            null
        }
    }

    /**
     * 是否有存储权限
     */
    @JvmStatic
    fun isGrantedStoragePermission(context: Context?): Boolean {
        return if (isAndroid11) {
            Environment.isExternalStorageManager()
        } else {
            val storage = Permission.Group.STORAGE
            isGrantedPermission(context, storage)
        }
    }

     fun isGrantedPermission(context: Context?, permissions: Array<String>): Boolean {
         val permissionList: MutableList<String> = ArrayList()
         for (group in permissions) {
             permissionList.add(group)
         }
         return isGrantedPermission(context!!,permissionList)

    }

    /**
     * 是否有安装权限
     */
    @JvmStatic
    fun isGrantedInstallPermission(context: Context): Boolean {
        return if (isAndroid8) {
            context.packageManager.canRequestPackageInstalls()
        } else true
    }

    /**
     * 是否有悬浮窗权限
     */
    @JvmStatic
    fun isGrantedWindowPermission(context: Context?): Boolean {
        return if (isAndroid6) {
            Settings.canDrawOverlays(context)
        } else true
    }

    /**
     * 是否有通知栏权限
     */
    @JvmStatic
    fun isGrantedNotifyPermission(context: Context): Boolean {
        if (isAndroid7) {
            return context.getSystemService(NotificationManager::class.java)
                .areNotificationsEnabled()
        }
        if (isAndroid6) {
            // 参考 Support 库中的方法： NotificationManagerCompat.from(context).areNotificationsEnabled()
            val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            return try {
                val method = appOps.javaClass.getMethod(
                    "checkOpNoThrow",
                    Integer.TYPE,
                    Integer.TYPE,
                    String::class.java
                )
                val field = appOps.javaClass.getDeclaredField("OP_POST_NOTIFICATION")
                val value = field[Int::class.java] as Int
                method.invoke(
                    appOps,
                    value,
                    context.applicationInfo.uid,
                    context.packageName
                ) as Int == AppOpsManager.MODE_ALLOWED
            } catch (ignored: NoSuchMethodException) {
                true
            } catch (ignored: NoSuchFieldException) {
                true
            } catch (ignored: InvocationTargetException) {
                true
            } catch (ignored: IllegalAccessException) {
                true
            } catch (ignored: RuntimeException) {
                true
            }
        }
        return true
    }

    /**
     * 是否有系统设置权限
     */
    @JvmStatic
    fun isGrantedSettingPermission(context: Context?): Boolean {
        return if (isAndroid6) {
            Settings.System.canWrite(context)
        } else true
    }

    /**
     * 判断某个权限集合是否包含特殊权限
     */
    @JvmStatic
    fun containsSpecialPermission(permissions: List<String>?): Boolean {
        if (permissions == null || permissions.isEmpty()) {
            return false
        }
        for (permission in permissions) {
            if (isSpecialPermission(permission)) {
                return true
            }
        }
        return false
    }

    /**
     * 判断某个权限是否是特殊权限
     */
    @JvmStatic
    fun isSpecialPermission(permission: String): Boolean {
        return Permission.MANAGE_EXTERNAL_STORAGE == permission || Permission.REQUEST_INSTALL_PACKAGES == permission || Permission.SYSTEM_ALERT_WINDOW == permission || Permission.NOTIFICATION_SERVICE == permission || Permission.WRITE_SETTINGS == permission
    }

    /**
     * 判断某些权限是否全部被授予
     */
    fun isGrantedPermission(context: Context, permissions:List<String>?): Boolean {
        // 如果是安卓 6.0 以下版本就直接返回 true
        if (!isAndroid6) {
            return true
        }
        if (permissions!=null&&permissions.isNotEmpty()){
            for (permission in permissions) {
                if (!isGrantedPermission(context, permission)) {
                    return false
                }
            }
        }

        return true
    }

    /**
     * 判断某个权限是否授予
     */
    @JvmStatic
    fun isGrantedPermission(context: Context, permission: String): Boolean {
        // 如果是安卓 6.0 以下版本就默认授予
        if (!isAndroid6) {
            return true
        }

        // 检测存储权限
        if (Permission.MANAGE_EXTERNAL_STORAGE == permission) {
            return isGrantedStoragePermission(context)
        }

        // 检测安装权限
        if (Permission.REQUEST_INSTALL_PACKAGES == permission) {
            return isGrantedInstallPermission(context)
        }

        // 检测悬浮窗权限
        if (Permission.SYSTEM_ALERT_WINDOW == permission) {
            return isGrantedWindowPermission(context)
        }

        // 检测通知栏权限
        if (Permission.NOTIFICATION_SERVICE == permission) {
            return isGrantedNotifyPermission(context)
        }

        // 检测系统权限
        if (Permission.WRITE_SETTINGS == permission) {
            return isGrantedSettingPermission(context)
        }
        if (!isAndroid10) {
            // 检测 10.0 的三个新权限
            if (Permission.ACCESS_BACKGROUND_LOCATION == permission || Permission.ACCESS_MEDIA_LOCATION == permission) {
                return true
            }
            if (Permission.ACTIVITY_RECOGNITION == permission) {
                return context.checkSelfPermission(Permission.BODY_SENSORS) == PackageManager.PERMISSION_GRANTED
            }
        }
        if (!isAndroid9) {
            // 检测 9.0 的一个新权限
            if (Permission.ACCEPT_HANDOVER == permission) {
                return true
            }
        }
        if (!isAndroid8) {
            // 检测 8.0 的两个新权限
            if (Permission.ANSWER_PHONE_CALLS == permission) {
                return context.checkSelfPermission(Permission.PROCESS_OUTGOING_CALLS) == PackageManager.PERMISSION_GRANTED
            }
            if (Permission.READ_PHONE_NUMBERS == permission) {
                return context.checkSelfPermission(Permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
            }
        }
        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 获取某个权限的状态
     *
     * @return        已授权返回  [PackageManager.PERMISSION_GRANTED]
     * 未授权返回  [PackageManager.PERMISSION_DENIED]
     */
    @JvmStatic
    fun getPermissionStatus(context: Context, permission: String): Int {
        return if (isGrantedPermission(
                context,
                permission
            )
        ) PackageManager.PERMISSION_GRANTED else PackageManager.PERMISSION_DENIED
    }

    /**
     * 在权限组中检查是否有某个权限是否被永久拒绝
     *
     * @param activity              FragmentActivity对象
     * @param permissions            请求的权限
     */
    fun isPermissionPermissionDenied(
        activity: FragmentActivity,
        permissions: List<String>
    ): Boolean {
        for (permission in permissions) {
            if (isPermissionPermanentDenied(activity, permission)) {
                return true
            }
        }
        return false
    }

    /**
     * 判断某个权限是否被永久拒绝
     *
     * @param activity              FragmentActivity对象
     * @param permission            请求的权限
     */
    private fun isPermissionPermanentDenied(
        activity: FragmentActivity,
        permission: String
    ): Boolean {
        if (!isAndroid6) {
            return false
        }

        // 特殊权限不算，本身申请方式和危险权限申请方式不同，因为没有永久拒绝的选项，所以这里返回 false
        if (isSpecialPermission(permission)) {
            return false
        }
        if (!isAndroid10) {
            // 检测 10.0 的三个新权限
            if (Permission.ACCESS_BACKGROUND_LOCATION == permission || Permission.ACCESS_MEDIA_LOCATION == permission) {
                return false
            }
            if (Permission.ACTIVITY_RECOGNITION == permission) {
                return activity.checkSelfPermission(Permission.BODY_SENSORS) == PackageManager.PERMISSION_DENIED &&
                        !activity.shouldShowRequestPermissionRationale(permission)
            }
        }
        if (!isAndroid9) {
            // 检测 9.0 的一个新权限
            if (Permission.ACCEPT_HANDOVER == permission) {
                return false
            }
        }
        if (!isAndroid8) {
            // 检测 8.0 的两个新权限，如果当前版本不符合最低要求，那么就用旧权限进行检测
            if (Permission.ANSWER_PHONE_CALLS == permission) {
                return activity.checkSelfPermission(Permission.PROCESS_OUTGOING_CALLS) == PackageManager.PERMISSION_DENIED &&
                        !activity.shouldShowRequestPermissionRationale(permission)
            }
            if (Permission.READ_PHONE_NUMBERS == permission) {
                return activity.checkSelfPermission(Permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED &&
                        !activity.shouldShowRequestPermissionRationale(permission)
            }
        }
        return activity.checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED &&
                !activity.shouldShowRequestPermissionRationale(permission)
    }

    /**
     * 获取没有授予的权限
     *
     * @param permissions           需要请求的权限组
     * @param grantResults          允许结果组
     */
    @JvmStatic
    fun getDeniedPermissions(permissions: Array<String>, grantResults: IntArray): List<String> {
        val deniedPermissions: MutableList<String> = ArrayList()
        for (i in grantResults.indices) {
            // 把没有授予过的权限加入到集合中
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(permissions[i])
            }
        }
        return deniedPermissions
    }

    /**
     * 获取已授予的权限
     *
     * @param permissions       需要请求的权限组
     * @param grantResults      允许结果组
     */
    @JvmStatic
    fun getGrantedPermissions(permissions: Array<String>, grantResults: IntArray): List<String> {
        val grantedPermissions: MutableList<String> = ArrayList()
        for (i in grantResults.indices) {
            // 把授予过的权限加入到集合中
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                grantedPermissions.add(permissions[i])
            }
        }
        return grantedPermissions
    }

    /**
     * 处理和优化已经过时的权限
     */
    @JvmStatic
    fun optimizeDeprecatedPermission(permission: MutableList<String>) {
        // 如果本次申请包含了 Android 11 存储权限
        if (permission.contains(Permission.MANAGE_EXTERNAL_STORAGE)) {
            require(
                !(permission.contains(Permission.READ_EXTERNAL_STORAGE) ||
                        permission.contains(Permission.WRITE_EXTERNAL_STORAGE))
            ) {
                // 检测是否有旧版的存储权限，有的话直接抛出异常，请不要自己动态申请这两个权限
                "Please do not apply for these two permissions dynamically"
            }
            if (!isAndroid11) {
                // 自动添加旧版的存储权限，因为旧版的系统不支持申请新版的存储权限
                permission.add(Permission.READ_EXTERNAL_STORAGE)
                permission.add(Permission.WRITE_EXTERNAL_STORAGE)
            }
        }
        if (permission.contains(Permission.ANSWER_PHONE_CALLS)) {
            require(!permission.contains(Permission.PROCESS_OUTGOING_CALLS)) {
                // 检测是否有旧版的接听电话权限，有的话直接抛出异常，请不要自己动态申请这个权限
                "Please do not apply for these two permissions dynamically"
            }
            if (!isAndroid10 && !permission.contains(Permission.PROCESS_OUTGOING_CALLS)) {
                // 自动添加旧版的接听电话权限，因为旧版的系统不支持申请新版的权限
                permission.add(Permission.PROCESS_OUTGOING_CALLS)
            }
        }
        if (permission.contains(Permission.ACTIVITY_RECOGNITION)) {
            if (!isAndroid10 && !permission.contains(Permission.BODY_SENSORS)) {
                // 自动添加传感器权限，因为这个权限是从 Android 10 开始才从传感器权限中剥离成独立权限
                permission.add(Permission.BODY_SENSORS)
            }
        }
    }

    /**
     * 判断这个意图的 Activity 是否存在
     */
    fun areActivityIntent(context: Context, intent: Intent?): Boolean {
        return !context.packageManager.queryIntentActivities(
            intent!!,
            PackageManager.MATCH_DEFAULT_ONLY
        ).isEmpty()
    }

    /**
     * 当前是否运行在 Debug 模式下
     */
    @JvmStatic
    fun isDebugMode(context: Context): Boolean {
        return context.applicationInfo != null &&
                context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
    }

    /**
     * 将数组转换成 ArrayList
     *
     * 这里解释一下为什么不用 Arrays.asList
     * 第一是返回的类型不是 java.util.ArrayList 而是 java.util.Arrays.ArrayList
     * 第二是返回的 ArrayList 对象是只读的，也就是不能添加任何元素，否则会抛异常
     */
    @JvmStatic
    fun <String> asArrayList(vararg array: String): ArrayList<String>? {
        var list: ArrayList<String> = ArrayList(array.size)
        for (t in array) {
            list.add(t)
        }
        return list
    }

    /**
     * 检测权限有没有在清单文件中注册
     *
     * @param activity              Activity对象
     * @param requestPermissions    请求的权限组
     */
    @JvmStatic
    fun checkPermissionManifest(activity: Activity, requestPermissions: MutableList<String>?) {
        val manifestPermissions = getManifestPermissions(activity)
        if (manifestPermissions == null || manifestPermissions.isEmpty()) {
            throw ManifestException()
        }
        val minSdkVersion: Int
        minSdkVersion = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            activity.applicationInfo.minSdkVersion
        } else {
            Build.VERSION_CODES.M
        }
        for (permission in requestPermissions!!) {
            if (minSdkVersion < Build.VERSION_CODES.R) {
                if (Permission.MANAGE_EXTERNAL_STORAGE == permission) {
                    if (!manifestPermissions.contains(Permission.READ_EXTERNAL_STORAGE)) {
                        // 为了保证能够在旧版的系统上正常运行，必须要在清单文件中注册此权限
                        throw ManifestException(Permission.READ_EXTERNAL_STORAGE)
                    }
                    if (!manifestPermissions.contains(Permission.WRITE_EXTERNAL_STORAGE)) {
                        // 为了保证能够在旧版的系统上正常运行，必须要在清单文件中注册此权限
                        throw ManifestException(Permission.WRITE_EXTERNAL_STORAGE)
                    }
                }
            }
            if (minSdkVersion < Build.VERSION_CODES.Q) {
                if (Permission.ACTIVITY_RECOGNITION == permission) {
                    if (!manifestPermissions.contains(Permission.BODY_SENSORS)) {
                        // 为了保证能够在旧版的系统上正常运行，必须要在清单文件中注册此权限
                        throw ManifestException(Permission.BODY_SENSORS)
                    }
                }
            }
            if (minSdkVersion < Build.VERSION_CODES.O) {
                if (Permission.ANSWER_PHONE_CALLS == permission) {
                    if (!manifestPermissions.contains(Permission.PROCESS_OUTGOING_CALLS)) {
                        // 为了保证能够在旧版的系统上正常运行，必须要在清单文件中注册此权限
                        throw ManifestException(Permission.PROCESS_OUTGOING_CALLS)
                    }
                }
                if (Permission.READ_PHONE_NUMBERS == permission) {
                    if (!manifestPermissions.contains(Permission.READ_PHONE_STATE)) {
                        // 为了保证能够在旧版的系统上正常运行，必须要在清单文件中注册此权限
                        throw ManifestException(Permission.READ_PHONE_STATE)
                    }
                }
            }
            if (Permission.NOTIFICATION_SERVICE == permission) {
                // 不检测通知栏权限有没有在清单文件中注册，因为这个权限是虚拟出来的，有没有在清单文件中注册都没关系
                continue
            }
            if (!manifestPermissions.contains(permission)) {
                throw ManifestException(permission)
            }
        }
    }

    /**
     * 检查targetSdkVersion 是否符合要求
     *
     * @param context                   上下文对象
     * @param requestPermissions        请求的权限组
     */
    @JvmStatic
    fun checkTargetSdkVersion(context: Context, requestPermissions: MutableList<String>) {
        // targetSdk 最低版本要求
        val targetSdkMinVersion: Int
        targetSdkMinVersion =
            if (requestPermissions.contains(Permission.MANAGE_EXTERNAL_STORAGE)) {
                // 必须设置 targetSdkVersion >= 30 才能正常检测权限，否则请使用 Permission.Group.STORAGE 来申请存储权限
                Build.VERSION_CODES.R
            } else if (requestPermissions.contains(Permission.ACCEPT_HANDOVER)) {
                Build.VERSION_CODES.P
            } else if (requestPermissions.contains(Permission.ACCESS_BACKGROUND_LOCATION) ||
                requestPermissions.contains(Permission.ACTIVITY_RECOGNITION) ||
                requestPermissions.contains(Permission.ACCESS_MEDIA_LOCATION)
            ) {
                Build.VERSION_CODES.Q
            } else if (requestPermissions.contains(Permission.REQUEST_INSTALL_PACKAGES) ||
                requestPermissions.contains(Permission.ANSWER_PHONE_CALLS) ||
                requestPermissions.contains(Permission.READ_PHONE_NUMBERS)
            ) {
                Build.VERSION_CODES.O
            } else {
                Build.VERSION_CODES.M
            }

        // 必须设置正确的 targetSdkVersion 才能正常检测权限
        if (context.applicationInfo.targetSdkVersion < targetSdkMinVersion) {
            throw RuntimeException("The targetSdkVersion SDK must be $targetSdkMinVersion or more")
        }
    }// 请求码必须在 2 的 16 次方以内

    /**
     * 获得随机的 RequestCode
     */
    @JvmStatic
    val randomRequestCode: Int
        get() =// 请求码必须在 2 的 16 次方以内
            Random().nextInt(Math.pow(2.0, 16.0).toInt())

    /**
     * 获取上下文中的 Activity 对象
     */
    @JvmStatic
    fun getFragmentActivity(context: Context?): FragmentActivity? {
        var context = context
        do {
            context = if (context is FragmentActivity) {
                return context
            } else if (context is ContextWrapper) {
                context.baseContext
            } else {
                return null
            }
        } while (context != null)
        return null
    }


}