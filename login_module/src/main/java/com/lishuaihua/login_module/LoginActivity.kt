package com.lishuaihua.login_module

import android.os.Bundle
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.lishuaihua.baselib.base.BaseActivity
import com.lishuaihua.baselib.base.BaseApplication
import com.lishuaihua.baselib.binding.binding
import com.lishuaihua.login_module.databinding.ActivityLoginBinding

@Route(path ="/login/login")
class LoginActivity : BaseActivity<LoginViewModel>() {
    private val binding: ActivityLoginBinding by binding()
    override fun getLayoutResId(): Int {
        return R.layout.activity_login
    }

    override fun doCreateView(savedInstanceState: Bundle?) {
        binding.navigationBar.setTitle("登录")
        binding.navigationBar.setBackClickListener(this)
        vm.liveData.observe(this, {
            binding.tvResult.text = it.toString()
        })
        vm.codeLiveData.observe(this, {
            Toast.makeText(this@LoginActivity, "发送验证码", Toast.LENGTH_SHORT).show()
        })
        vm.getVerficationCode("18924138696")
        binding.login.setOnClickListener {
            vm.login("18924138696", "123456")
        }

    }


}