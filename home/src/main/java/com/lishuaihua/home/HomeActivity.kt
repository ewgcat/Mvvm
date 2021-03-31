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
import com.vector.update_app.*
import com.vector.update_app.utils.AppUpdateUtils
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
        binding.tvUpdateApk.setOnClickListener {
            checkUpdate()
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


    /**
     * 更新接口请求
     */
    fun checkUpdate() {
        val path = Environment.getExternalStorageDirectory().absolutePath
        val params: MutableMap<String, String> = HashMap()
        params["systemType"] = "1"
        params["edition"] = AppUpdateUtils.getVersionCode(this).toString()
        val versionCode = AppUpdateUtils.getVersionCode(this) //获取当前版本号 - 100
        val ver = versionCode - 100 //当前版本号
        UpdateAppManager.Builder() //必须设置，当前Activity
            .setActivity(this) //必须设置，实现httpManager接口的对象
            .setHttpManager(OkGoUpdateHttpUtil()) //必须设置，更新地址
            .setUpdateUrl("http://app.fbrx.cn/v1/passport/tool/appQuery") //添加自定义参数
            .setParams(params) //                .setJsonParams("{\"systemType\":1,\"edition\":1}")//TODO setJsonParams("{\"systemType\":1,\"edition\":"+AppUpdateUtils.getVersionCode(this)+"}")
            .setJsonParams("{\"systemType\":1,\"edition\":$ver}") //post请求
            .setPost(true) //设置apk下砸路径，默认是在下载到sd卡下/Download/1.0.0/test.apk
            .setTargetPath(path)
            .build() //检测是否有新版本
            .checkNewApp(object : UpdateCallback() {
                /**
                 * 解析json,自定义协议
                 *
                 * @param json 服务器返回的json
                 * @return UpdateAppBean
                 */
                override fun parseJson(json: String): UpdateAppBean {
                    Log.d("YYT", "json:$json")
                    val updateAppBean = UpdateAppBean()
                    try {
                        val appUpdateBean = Gson().fromJson(
                            json,
                            AppUpdateBean::class.java
                        )
                        if (appUpdateBean.data != null) { //有更新
                            if (appUpdateBean.data.type == 2) { //强制更新
                                updateAppBean //（必须）是否更新Yes,No
                                    .setUpdate("Yes") //（必须）新版本号，
                                    .setNewVersion(appUpdateBean.data.editionNum) //（必须）下载地址
                                    //                                            .setApkFileUrl(appUpdateBean.getData().getUrl())
                                    .setApkFileUrl("https://fubangrongxing-prod.oss-cn-beijing.aliyuncs.com/backstage/test/a08062a9-cd4f-408e-a52f-af3c31b0fd07.apk") //（必须）更新内容
                                    .setUpdateLog("1.紧急修复优惠返利bug 2.完善体验店模块").isConstraint = true
                            } else { //非强制更新更新
                                updateAppBean //（必须）是否更新Yes,No
                                    .setUpdate("Yes") //（必须）新版本号，
                                    .setNewVersion(appUpdateBean.data.editionNum) //（必须）下载地址
                                    //                                            .setApkFileUrl(appUpdateBean.getData().getUrl())
                                    .setApkFileUrl("https://fubangrongxing-prod.oss-cn-beijing.aliyuncs.com/backstage/test/a08062a9-cd4f-408e-a52f-af3c31b0fd07.apk") //（必须）更新内容
                                    .setUpdateLog("").isConstraint = false
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    return updateAppBean
                }

                /**
                 * 有新版本
                 *
                 * @param updateApp        新版本信息
                 * @param updateAppManager app更新管理器
                 */
                override fun hasNewApp(
                    updateApp: UpdateAppBean,
                    updateAppManager: UpdateAppManager
                ) {
                    //自定义对话框
                    //                        showDiyDialog(updateApp, updateAppManager);
                    updateAppManager.showDialogFragment()
                }

                /**
                 * 网络请求之前
                 */
                override fun onBefore() {
                    Log.d("YYT", "onBefore")
                    CProgressDialogUtils.showProgressDialog(this@HomeActivity)
                }

                /**
                 * 网路请求之后
                 */
                override fun onAfter() {
                    Log.d("YYT", "onAfter")
                    CProgressDialogUtils.cancelProgressDialog(this@HomeActivity)
                }

                /**
                 * 没有新版本
                 */
                override fun noNewApp(error: String) {
                    Log.d("YYT", "noNewApp")
                }
            })
    }


}