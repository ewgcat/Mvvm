package com.lishuaihua.mvvmframework

import android.Manifest
import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.lishuaihua.baselib.base.BaseActivity
import com.lishuaihua.baselib.binding.ext.viewbind
import com.lishuaihua.mvvmframework.databinding.ActivitySplashBinding
import com.lishuaihua.permissions.JackPermissions
import com.lishuaihua.permissions.OnPermission

class SplashActivity : BaseActivity() {

    internal var permissions = arrayOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.CALL_PHONE,
        Manifest.permission.CAMERA,
        Manifest.permission.MANAGE_EXTERNAL_STORAGE,
        Manifest.permission.CHANGE_NETWORK_STATE,
        Manifest.permission.CHANGE_WIFI_STATE,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_NETWORK_STATE
    )

    /**
     * 运行时请求权限
     */
    private fun initRxPermissions(permissions: Array<String>) {
        JackPermissions.with(this) // 申请安装包权限
            .permission(permissions) // 申请多个权限
            .request(object : OnPermission {
                override fun onGranted(permissions: List<String>?, all: Boolean) {
                    if (all) {
                        ARouter.getInstance().build("/home/home").navigation()
                        finish()
                    }
                }

                override fun onDenied(permissions: List<String>?, never: Boolean) {

                }
            })
    }

    private val binding: ActivitySplashBinding by viewbind()

    override fun getLayoutResId(): Int = R.layout.activity_splash
    override fun doCreateView(savedInstanceState: Bundle?) {
        initRxPermissions(permissions)


    }
}