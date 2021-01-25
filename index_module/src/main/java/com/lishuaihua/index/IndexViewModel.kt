package com.lishuaihua.index

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingSource
import com.lishuaihua.baselib.util.MD5Util
import com.lishuaihua.net.httputils.BaseViewModel
import com.lishuaihua.net.httputils.HttpUtils
import com.lishuaihua.index.bean.IndexData
import com.lishuaihua.index.bean.ItemListItem
import com.lishuaihua.index.service.IndexService
import com.lishuaihua.paging3.SimplePager
import okhttp3.RequestBody
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class IndexViewModel : BaseViewModel() {

    var indexLiveData = MutableLiveData<IndexData>()
    private val indexService = HttpUtils.create(IndexService::class.java)

    fun index(page: Int, pageSize: Int) {
        launch({
            var data = JSONObject()
            data.put("platform", 1)
            data.put("terminal", 2)
            data.put("type", "hotRecommend")
            data.put("page", page)
            data.put("limit", pageSize)
            val body = getBaseParams(data)
            var date = Date()
            val format = SimpleDateFormat("yyyyMMddHHmm")
            var reqDate = format.format(date)
            var sign =
                MD5Util.MD5("jiaomigo.gialen.com#2019|" + "" + "|" + reqDate + "|" + "app/req/shop.ald")
            indexService.index(
                "http://cs-jiaomigo.gialen.com/gateway/app/req/shop.ald", sign,
                reqDate,
                "0",
                "0",
                body
            )
        }, indexLiveData, true)
    }

    var page: Int = 1
    var pageSize: Int = 2
    var jsonObject=JSONObject()


    private val pager = object : SimplePager<JSONObject, ItemListItem>(pageSize, jsonObject) {
        override suspend fun loadData(params: PagingSource.LoadParams<JSONObject>): PagingSource.LoadResult<JSONObject, ItemListItem> {
            val key = params.key ?: return PagingSource.LoadResult.Page(emptyList(), null, null)
            jsonObject.put("platform", 1)
            jsonObject.put("terminal", 2)
            jsonObject.put("type", "hotRecommend")
            jsonObject.put("limit", pageSize)
            jsonObject.put("page", page)
            val baseParams = getBaseParams(jsonObject)
            return try {

                var date = Date()
                val format = SimpleDateFormat("yyyyMMddHHmm")
                var reqDate = format.format(date)
                var sign = MD5Util.MD5("jiaomigo.gialen.com#2019|" + "" + "|" + reqDate + "|" + "app/req/shop.ald")
                val result=  indexService.index(
                    "http://cs-jiaomigo.gialen.com/gateway/app/req/shop.ald", sign,
                    reqDate,
                    "0",
                    "0",
                    baseParams
                )
                val indexData = result.data as IndexData
                var list=indexData.itemList as List<ItemListItem>
                ++page
                jsonObject.put("page", page)
                PagingSource.LoadResult.Page(list, null, null)
            } catch (e: Exception) {
                PagingSource.LoadResult.Error(e)
            }

        }
    }

    val data = pager.getData(viewModelScope)

}