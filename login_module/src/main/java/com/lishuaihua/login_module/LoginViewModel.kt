package com.lishuaihua.login_module

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson

import com.lishuaihua.baselib.util.MD5Util
import com.lishuaihua.net.httputils.BaseViewModel

import com.lishuaihua.net.httputils.HttpUtils

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class LoginViewModel : BaseViewModel() {

    var liveData = MutableLiveData<UserInfo>()
    var codeLiveData = MutableLiveData<String>()


    private val loginService = HttpUtils.create(LoginService::class.java)

    fun login(phone: String, code: String) {
        launch({
            var date = Date()
            val format = SimpleDateFormat("yyyyMMddHHmm")
            var reqDate = format.format(date)
            var sign =
                MD5Util.MD5("jiaomigo.gialen.com#2019|" + phone + "|" + reqDate + "|" + "app/login")
            var jsonObject = JSONObject()
            jsonObject.put("phone", phone)
            jsonObject.put("verificationCode", code)
            val baseParams = getBaseParams(jsonObject)
            loginService.postLogin(
                "https://apigw.gialen.com/app/login", sign,
                reqDate,
                "0",
                "0",
                baseParams
            )
        },liveData,true)
    }


    fun getVerficationCode(phone: String) {
        launch({
            var date = Date()
            val format = SimpleDateFormat("yyyyMMddHHmm")
            var reqDate = format.format(date)
            var sign = MD5Util.MD5("jiaomigo.gialen.com#2019|" + "" + "|" + reqDate + "|" + "app/req/user.getLoginRegistryVerification")
            var jsonObject = JSONObject()
            jsonObject.put("phone", phone)
            loginService.getVerficationCode(
                "https://apigw.gialen.com/app/req/user.getLoginRegistryVerification",
                sign,
                reqDate,
                getBaseParams(jsonObject)
            )},codeLiveData,false)
    }

}



