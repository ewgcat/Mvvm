package com.lishuaihua.mvvmframework

import android.os.Bundle
import coil.load
import coil.transform.CircleCropTransformation
import com.lishuaihua.baselib.base.BaseActivity
import com.lishuaihua.baselib.base.BaseViewModel
import com.lishuaihua.baselib.binding.binding
import com.lishuaihua.mvvmframework.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity<BaseViewModel>() {

    private val binding: ActivitySplashBinding by binding()

    override fun getLayoutResId(): Int=R.layout.activity_splash
    override fun doCreateView(savedInstanceState: Bundle?) {
        //资源文件
        binding.imageView.load(R.mipmap.ic_launcher)
        //加载网络资源
        binding. imageView2.load("https://www.example.com/image.jpg") {
            crossfade(true)
            placeholder(R.mipmap.ic_launcher)
            transformations(CircleCropTransformation())
        }
    }
}