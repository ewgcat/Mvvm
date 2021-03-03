package com.lishuaihua.home

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.lishuaihua.baselib.autoservice.IWebViewService
import com.lishuaihua.baselib.autoservice.ServiceLoaderHelper
import com.lishuaihua.baselib.base.BaseActivity
import com.lishuaihua.baselib.binding.ext.viewbind
import com.lishuaihua.home.databinding.ActivityHomeBinding


@Route(path = "/home/home")
class HomeActivity : BaseActivity() {
    private val binding: ActivityHomeBinding by viewbind()
    override fun getLayoutResId(): Int = R.layout.activity_home

    override fun doCreateView(savedInstanceState: Bundle?) {
        binding.tvBrowser.setOnClickListener {
            val webviewService = ServiceLoaderHelper.load(
                IWebViewService::class.java
            )
            webviewService?.startDemoHtml(this)
        }
        binding.login.setOnClickListener {
            ARouter.getInstance().build("/login/login").navigation()
        }
        binding.page3.setOnClickListener {
            ARouter.getInstance().build("/index/index").withTransition(0, 0).navigation()
        }
        binding.tvWorkManager.setOnClickListener {
            ARouter.getInstance().build("/workmanagerlib/test").navigation()
        }
        binding.tvRtmpLive.setOnClickListener {
            ARouter.getInstance().build("/video/video").navigation()
        }



    }


}