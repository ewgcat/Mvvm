package com.lishuaihua.login_module

import android.os.Bundle
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.lishuaihua.baselib.base.BaseViewModelActivity
import com.lishuaihua.baselib.binding.ext.viewbind
import com.lishuaihua.baselib.eventbus.LoginEvent
import com.lishuaihua.baselib.util.StringUtils
import com.lishuaihua.login_module.databinding.ActivityLoginBinding
import org.greenrobot.eventbus.EventBus

@Route(path = "/login/login")
class LoginActivity : BaseViewModelActivity<LoginViewModel>() {
    private val binding: ActivityLoginBinding by viewbind()
    override fun getLayoutResId(): Int {
        return R.layout.activity_login
    }

    override fun doCreateView(savedInstanceState: Bundle?) {
        binding.navigationBar.setTitle("登录")
        binding.navigationBar.setBackClickListener(this)
        vm.liveData.observe(this, {
//            binding.tvResult.text = it.toString()
            EventBus.getDefault().post(LoginEvent(it.toString()))
            finish()
        })
        vm.codeLiveData.observe(this, {
            Toast.makeText(this@LoginActivity, "发送验证码", Toast.LENGTH_SHORT).show()
        })
        binding.tvGetCheckCode.setOnClickListener{
            val phone = binding.etPhone.text.toString()
            if (!StringUtils.isEmpty(phone)){
                vm.getVerficationCode(phone)
            }
        }

        binding.login.setOnClickListener {
            val phone = binding.etPhone.text.toString()
            val code = binding.etCode.text.toString()
            if (!StringUtils.isEmpty(phone)&&!StringUtils.isEmpty(code)){
                vm.login(phone, code)
            }
        }

    }


}