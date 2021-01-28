package com.lishuaihua.main_module

import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.lishuaihua.baselib.base.BaseActivity
import com.lishuaihua.baselib.binding.binding
import com.lishuaihua.main_module.databinding.ActivityHomeBinding
import com.lishuaihua.net.httputils.BaseViewModel

class HomeActivity : BaseActivity<BaseViewModel>() {
    private val binding: ActivityHomeBinding by binding()

    override fun getLayoutResId(): Int =R.layout.activity_home

    override fun doCreateView(savedInstanceState: Bundle?) {
        binding.tvBrowser.setOnClickListener{
            ARouter.getInstance().build("/webview_module/test").navigation()
        }
        binding.login.setOnClickListener{
            ARouter.getInstance().build("/login/login").navigation()
        }
        binding.page3.setOnClickListener{
            ARouter.getInstance().build("/index/index").navigation()
        }
        binding.tvWorkManager.setOnClickListener{
            ARouter.getInstance().build("/workmanagerlib/test").navigation()
        }

    }

}