package com.lishuaihua.net.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.QueryMap
import retrofit2.http.Url

interface ApiService {
    @GET
    suspend fun doGet(@Url url: String): ResponseBody

    @GET
    suspend fun doGet(@Url url: String, @QueryMap map: Map<String, String>): ResponseBody

    @FormUrlEncoded
    @POST
    suspend fun doPost(@Url url: String, @FieldMap map: Map<String, String>): ResponseBody

    @Multipart
    @POST
    suspend fun doPostImages(@Url url: String, @PartMap map: Map<String, RequestBody>, @Part requestBodyMap: List<MultipartBody.Part>): ResponseBody

    @POST
    suspend fun doBodyPost(@Url url: String, @Body body: RequestBody): ResponseBody
}
