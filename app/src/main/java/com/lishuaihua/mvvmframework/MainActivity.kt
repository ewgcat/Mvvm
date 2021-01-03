package com.lishuaihua.mvvmframework

import android.os.Bundle
import com.lishuaihua.baselib.base.BaseActivity
import com.lishuaihua.baselib.ext.viewbind
import com.lishuaihua.mvvmframework.databinding.ActivityMainBinding

class MainActivity : BaseActivity< MainViewModel>() {
    private val binding: ActivityMainBinding by viewbind()
    override fun getLayoutResId(): Int =R.layout.activity_main
    override fun doCreateView(savedInstanceState: Bundle?) {
        binding.let {
            it.tv.text =vm.text.value
        }

    }

}