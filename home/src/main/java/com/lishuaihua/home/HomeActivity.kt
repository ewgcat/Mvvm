package com.lishuaihua.home

import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.Gson
import com.lishuaihua.baselib.autoservice.IWebViewService
import com.lishuaihua.baselib.autoservice.ServiceLoaderHelper
import com.lishuaihua.baselib.base.BaseActivity
import com.lishuaihua.baselib.base.BaseViewModelActivity
import com.lishuaihua.baselib.binding.ext.viewbind
import com.lishuaihua.data_module.greendao.GreenDaoDataManager
import com.lishuaihua.data_module.model.KeyWordBean
import com.lishuaihua.data_module.model.User
import com.lishuaihua.data_module.objectbox.ObjectBoxDataManager
import com.lishuaihua.data_module.model.UserInfo
import com.lishuaihua.data_module.room.RoomDataManager
import com.lishuaihua.home.databinding.ActivityHomeBinding
import kotlinx.coroutines.launch


@Route(path = "/home/home")
class HomeActivity : BaseViewModelActivity<HomeViewModel>() {
    private val binding: ActivityHomeBinding by viewbind()
    override fun getLayoutResId(): Int = R.layout.activity_home

    override fun doCreateView(savedInstanceState: Bundle?) {
        binding.tvBrowser.setOnClickListener {
            val webviewService = ServiceLoaderHelper.load(
                IWebViewService::class.java
            )
            webviewService?.startDemoHtml(this)
        }
        binding.list.setOnClickListener {
            ARouter.getInstance().build("/index/header").navigation()
        }
        binding.floatWindow.setOnClickListener {
            ARouter.getInstance().build("/float/float_window").navigation()
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
        binding.tvDragView.setOnClickListener {
            ARouter.getInstance().build("/home/drag").navigation()
        }
        binding.tvGreenDao.setOnClickListener {
            var keyWordBean=KeyWordBean()
            keyWordBean.keyword="关键词GreenDao"
            GreenDaoDataManager.getInstance().insertKeyWordBean(keyWordBean)
        }
        binding.tvObjectBox.setOnClickListener {
            var userInfo= UserInfo()
            userInfo.nickname="张三ObjectBox"
            userInfo.sex=30
            ObjectBoxDataManager.getInstance().insertUserInfo(userInfo)
        }
        binding.tvRoom.setOnClickListener {
            var user=User(1,"李四Room","Room")
            vm.insertUser(user)
        }


    }



}