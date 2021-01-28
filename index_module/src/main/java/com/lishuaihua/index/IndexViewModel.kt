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
import com.lishuaihua.paging3.adapter.DifferData
import com.lishuaihua.paging3.simple.SimplePager
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class IndexViewModel : BaseViewModel() {


    private val indexService = HttpUtils.create(IndexService::class.java)


    var page: Long = 1
    var pageSize: Int = 10

    val pager = SimplePager<Long, DifferData>(
        viewModelScope,
        enablePlaceholders = true
    ) {

        page = it.key ?: 1
        try {
            //从网络获取数据
            var jsonObject = JSONObject()
            jsonObject.put("platform", 1)
            jsonObject.put("terminal", 2)
            jsonObject.put("type", "hotRecommend")
            jsonObject.put("limit", pageSize)
            jsonObject.put("page", page)
            val baseParams = getBaseParams(jsonObject)
            var date = Date()
            val format = SimpleDateFormat("yyyyMMddHHmm")
            var reqDate = format.format(date)
            var sign =
                MD5Util.MD5("jiaomigo.gialen.com#2019|" + "" + "|" + reqDate + "|" + "app/req/shop.ald")
            val result = indexService.index(
                "https://apigw.gialen.com/app/req/shop.ald", sign,
                reqDate,
                "0",
                "0",
                baseParams
            )
            val indexData = result.data as IndexData
            var itemList = indexData.itemList .toMutableList()

            //返回数据
            PagingSource.LoadResult.Page(
                itemList,
                null,
                page+1,
                0,  //前面剩余多少未加载数量，
                100  //后面剩余多少未加载数量，配合 enablePlaceholders 在滑动过快的时候显示占位；
            )
        } catch (e: Exception) {
            //请求失败
            PagingSource.LoadResult.Error(e)
        }
    }


}