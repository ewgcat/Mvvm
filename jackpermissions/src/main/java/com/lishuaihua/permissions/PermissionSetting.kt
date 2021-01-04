package com.lishuaihua.permissions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

object PermissionSetting {
    /**
     * 根据传入的权限自动选择最合适的权限设置页
     */
    @JvmStatic
    fun getSmartPermissionIntent(context: Context, deniedPermissions: List<String>?): Intent? {
        if (deniedPermissions == null || deniedPermissions.isEmpty()) {
            return getApplicationDetailsIntent(context)
        }

        // 如果失败的权限里面包含了特殊权限
        if (PermissionUtils.containsSpecialPermission(deniedPermissions)) {
            // 如果当前只有一个权限被拒绝了
            if (deniedPermissions.size == 1) {
                val permission = deniedPermissions[0]
                if (Permission.MANAGE_EXTERNAL_STORAGE == permission) {
                    return getStoragePermissionIntent(context)
                }
                if (Permission.REQUEST_INSTALL_PACKAGES == permission) {
                    return getInstallPermissionIntent(context)
                }
                if (Permission.SYSTEM_ALERT_WINDOW == permission) {
                    return getWindowPermissionIntent(context)
                }
                if (Permission.NOTIFICATION_SERVICE == permission) {
                    return getNotifyPermissionIntent(context)
                }
                return if (Permission.WRITE_SETTINGS == permission) {
                    getSettingPermissionIntent(context)
                } else getApplicationDetailsIntent(context)
            }
            if (deniedPermissions.size == 3) {
                if (deniedPermissions.contains(Permission.MANAGE_EXTERNAL_STORAGE) &&
                    deniedPermissions.contains(Permission.READ_EXTERNAL_STORAGE) &&
                    deniedPermissions.contains(Permission.WRITE_EXTERNAL_STORAGE)
                ) {
                    return if (PermissionUtils.isAndroid11) {
                        getStoragePermissionIntent(context)
                    } else getApplicationDetailsIntent(
                        context
                    )
                }
            }
            return getApplicationDetailsIntent(context)
        }
        return getApplicationDetailsIntent(context)
    }

    /**
     * 获取应用详情界面意图
     */
    @JvmStatic
    fun getApplicationDetailsIntent(context: Context): Intent {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:" + context.packageName)
        return intent
    }

    /**
     * 获取安装权限设置界面意图
     */
    @JvmStatic
    fun getInstallPermissionIntent(context: Context): Intent? {
        var intent: Intent? = null
        if (PermissionUtils.isAndroid8) {
            intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
            intent.data = Uri.parse("package:" + context.packageName)
        }
        if (intent == null || !PermissionUtils.areActivityIntent(context, intent)) {
            intent = getApplicationDetailsIntent(context)
        }
        return intent
    }

    /**
     * 获取悬浮窗权限设置界面意图
     */
    @JvmStatic
    fun getWindowPermissionIntent(context: Context): Intent? {
        var intent: Intent? = null
        if (PermissionUtils.isAndroid6) {
            intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            // 在 Android 11 上面不能加包名跳转，因为就算加了也没有效果
            // 还有人反馈在 Android 11 的 TV 模拟器上会出现崩溃的情况
            // https://developer.android.google.cn/reference/android/provider/Settings#ACTION_MANAGE_OVERLAY_PERMISSION
            if (!PermissionUtils.isAndroid11) {
                intent.data = Uri.parse("package:" + context.packageName)
            }
        }
        if (intent == null || !PermissionUtils.areActivityIntent(context, intent)) {
            intent = getApplicationDetailsIntent(context)
        }
        return intent
    }

    /**
     * 获取通知栏权限设置界面意图
     */
    @JvmStatic
    fun getNotifyPermissionIntent(context: Context): Intent? {
        var intent: Intent? = null
        if (PermissionUtils.isAndroid8) {
            intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            //intent.putExtra(Settings.EXTRA_CHANNEL_ID, context.getApplicationInfo().uid);
        }
        if (intent == null || !PermissionUtils.areActivityIntent(context, intent)) {
            intent = getApplicationDetailsIntent(context)
        }
        return intent
    }

    /**
     * 获取系统设置权限界面意图
     */
    @JvmStatic
    fun getSettingPermissionIntent(context: Context): Intent? {
        var intent: Intent? = null
        if (PermissionUtils.isAndroid6) {
            intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.data = Uri.parse("package:" + context.packageName)
        }
        if (intent == null || !PermissionUtils.areActivityIntent(context, intent)) {
            intent = getApplicationDetailsIntent(context)
        }
        return intent
    }

    /**
     * 获取存储权限设置界面意图
     */
    @JvmStatic
    fun getStoragePermissionIntent(context: Context): Intent? {
        var intent: Intent? = null
        if (PermissionUtils.isAndroid11) {
            intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.data = Uri.parse("package:" + context.packageName)
        }
        if (intent == null || !PermissionUtils.areActivityIntent(context, intent)) {
            intent = getApplicationDetailsIntent(context)
        }
        return intent
    }
}