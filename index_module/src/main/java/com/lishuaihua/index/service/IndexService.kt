package com.lishuaihua.index.service

import com.lishuaihua.net.response.BaseResult
import com.lishuaihua.index.bean.IndexData
import okhttp3.RequestBody
import retrofit2.http.*
interface IndexService {
    @Headers("Content-Type: application/json", "Accept: application/json")//需要添加头
    @POST()
    suspend fun index(
        @Url url: String,
        @Header("sign") sign: String,
        @Header("reqDate") reqDate: String,
        @Header("latitude") latitude: String,
        @Header("longitude") longitude: String,
        @Body body: RequestBody
    ): BaseResult<IndexData>

}