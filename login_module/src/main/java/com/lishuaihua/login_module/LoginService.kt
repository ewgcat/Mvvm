package com.lishuaihua.login_module

import com.lishuaihua.net.response.BaseResult
import okhttp3.RequestBody
import retrofit2.http.*

interface LoginService {
    @Headers("Content-Type: application/json", "Accept: application/json")//需要添加头
    @POST()
    suspend fun postLogin(
        @Url url: String,
        @Header("sign") sign: String,
        @Header("reqDate") reqDate: String,
        @Header("latitude") latitude: String,
        @Header("longitude") longitude: String,
        @Body body: RequestBody
        ): BaseResult<UserInfo>

    /**
     * post请求
     * @param url
     * @param requestBody
     * @return
     */
    @Headers("Content-Type: application/json", "Accept: application/json")//需要添加头
    @POST()
    suspend  fun getVerficationCode(
        @Url url: String,
        @Header("sign") sign: String,
        @Header("reqDate") reqDate: String,
        @Body requestBody: RequestBody):BaseResult<String>
}