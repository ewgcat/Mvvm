package com.lishuaihua.mvvmframework

import android.os.Bundle
import com.lishuaihua.imageloader.load
import com.lishuaihua.imageloader.transform.CircleCropTransformation
import com.lishuaihua.baselib.base.BaseActivity
import com.lishuaihua.baselib.base.BaseViewModel
import com.lishuaihua.baselib.binding.binding
import com.lishuaihua.baselib.bus.LiveDataBus
import com.lishuaihua.baselib.sp.SharedPreferencesManager
import com.lishuaihua.mvvmframework.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity<BaseViewModel>() {

    private val binding: ActivitySplashBinding by binding()

    override fun getLayoutResId(): Int=R.layout.activity_splash
    override fun doCreateView(savedInstanceState: Bundle?) {
        binding.navigationBar.setTitle("主页")
        //资源文件
        binding.imageView.load(R.mipmap.ic_launcher)
        //加载网络资源
        binding.imageView2.load("https://www.example.com/image.jpg") {
            crossfade(true)
            placeholder(R.mipmap.ic_launcher)
            error(R.mipmap.ic_launcher)
            transformations(CircleCropTransformation())
        }
        binding.bt.setOnClickListener {
            var defaultGloableColor =
                SharedPreferencesManager.getInstance(SplashActivity@this).getInt("defaultGloableColor", 0)
            when(defaultGloableColor){
                1->  SharedPreferencesManager.getInstance(SplashActivity@this).saveInt("defaultGloableColor", 0)
                0->  SharedPreferencesManager.getInstance(SplashActivity@this).saveInt("defaultGloableColor", 1)
            }
            LiveDataBus.get().with("UpdateGloableColor", String::class.java)!!.postValue("UpdateGloableColor")
        }

    }
}