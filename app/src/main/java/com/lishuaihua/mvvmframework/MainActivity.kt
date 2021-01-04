package com.lishuaihua.mvvmframework

import android.Manifest
import android.os.Bundle
import com.lishuaihua.baselib.base.BaseActivity
import com.lishuaihua.baselib.ext.viewbind
import com.lishuaihua.mvvmframework.databinding.ActivityMainBinding
import com.lishuaihua.permissions.JackPermissions
import com.lishuaihua.permissions.OnPermission

class MainActivity : BaseActivity< MainViewModel>() {
    private val binding: ActivityMainBinding by viewbind()
    override fun getLayoutResId(): Int =R.layout.activity_main
    override fun doCreateView(savedInstanceState: Bundle?) {
        binding.let {
            it.tv.text =vm.text.value
        }
        initPermissions(permissions)

    }

    internal var permissions = arrayOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.CALL_PHONE,
        Manifest.permission.CAMERA,
        Manifest.permission.READ_CALENDAR,
        Manifest.permission.WRITE_CALENDAR,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.MANAGE_EXTERNAL_STORAGE,
        Manifest.permission.CHANGE_NETWORK_STATE,
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.READ_SMS
    )
    /**
     * 运行时请求权限
     */
    private fun initPermissions(permissions: Array<String>) {
        JackPermissions.with(this) // 申请安装包权限
            .permission(permissions) // 申请多个权限
            .request(object : OnPermission {
                override fun onGranted(permissions: List<String>?, all: Boolean) {


                }

                override fun onDenied(permissions: List<String>?, never: Boolean) {

                }

            })
    }

}


