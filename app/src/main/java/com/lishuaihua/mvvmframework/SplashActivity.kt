package com.lishuaihua.mvvmframework

import android.content.Intent
import android.os.Bundle
import com.lishuaihua.baselib.base.BaseActivity
import com.lishuaihua.baselib.binding.binding
import com.lishuaihua.mvvmframework.databinding.ActivitySplashBinding
import com.lishuaihua.net.httputils.BaseViewModel

class SplashActivity : BaseActivity<BaseViewModel>() {

    private val binding: ActivitySplashBinding by binding()

    override fun getLayoutResId(): Int = R.layout.activity_splash
    override fun doCreateView(savedInstanceState: Bundle?) {
        startActivity(Intent(SplashActivity@ this, MainActivity::class.java))
        finish()
    }
}