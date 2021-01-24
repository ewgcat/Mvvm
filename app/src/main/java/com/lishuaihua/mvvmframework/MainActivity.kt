package com.lishuaihua.mvvmframework

import android.content.Intent
import android.os.Bundle
import com.lishuaihua.baselib.base.BaseActivity
import com.lishuaihua.baselib.binding.binding
import com.lishuaihua.baselib.bus.LiveDataBus
import com.lishuaihua.baselib.sp.SharedPreferencesManager
import com.lishuaihua.imageloader.load
import com.lishuaihua.imageloader.transform.CircleCropTransformation
import com.lishuaihua.login_module.LoginActivity
import com.lishuaihua.mvvmframework.databinding.ActivityMainBinding
import com.lishuaihua.net.httputils.BaseViewModel
import com.lishuaihua.page3_module.IndexActivity

class MainActivity : BaseActivity<BaseViewModel>() {

    private val binding: ActivityMainBinding by binding()
    override fun getLayoutResId(): Int = R.layout.activity_main

    override fun doCreateView(savedInstanceState: Bundle?) {
        //资源文件
        binding.imageView.load(R.mipmap.ic_launcher)
        //加载网络资源
        binding.imageView2.load("https://www.example.com/image.jpg") {
            crossfade(true)
            placeholder(R.mipmap.ic_launcher)
            error(R.mipmap.ic_launcher)
            transformations(CircleCropTransformation())
        }
        binding.changeColor.setOnClickListener {
            var defaultGloableColor =
                SharedPreferencesManager.getInstance(MainActivity@ this)
                    .getInt("defaultGloableColor", 0)
            when (defaultGloableColor) {
                1 -> SharedPreferencesManager.getInstance(MainActivity@ this)
                    .saveInt("defaultGloableColor", 0)
                0 -> SharedPreferencesManager.getInstance(MainActivity@ this)
                    .saveInt("defaultGloableColor", 1)
            }
            LiveDataBus.get().with("UpdateGloableColor", String::class.java)!!
                .postValue("UpdateGloableColor")
        }
        binding.login.setOnClickListener {
            startActivity(Intent(MainActivity@ this, LoginActivity::class.java))
        }
        binding.page3.setOnClickListener {
            startActivity(Intent(MainActivity@ this, IndexActivity::class.java))
        }
    }
}