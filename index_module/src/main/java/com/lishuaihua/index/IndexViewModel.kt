package com.lishuaihua.index

import android.text.TextUtils
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
import kotlin.collections.ArrayList

class IndexViewModel : BaseViewModel() {

    var indexLiveData = MutableLiveData<IndexData>()
    var listLiveData = MutableLiveData<ArrayList<ItemListItem>>()

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
    var jsonObject = JSONObject()
    var itemList= ArrayList<ItemListItem>()
    private val mDate = Calendar.getInstance().apply {
        add(Calendar.DATE, 1)
    }



    private val pager = object : SimplePager<Int, ItemListItem>(pageSize, 10) {
        override suspend fun loadData(params: PagingSource.LoadParams<Int>): PagingSource.LoadResult<Int, ItemListItem> {
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
                val result = indexService.index(
                    "http://cs-jiaomigo.gialen.com/gateway/app/req/shop.ald", sign,
                    reqDate,
                    "0",
                    "0",
                    baseParams
                )
                val indexData = result.data as IndexData
                var list = indexData.itemList as ArrayList<ItemListItem>
                if (page==1){
                    itemList.clear()
                    itemList.addAll(list)
                    listLiveData.value=itemList
                }else{
                    itemList.addAll(list)
                }
                PagingSource.LoadResult.Page(itemList, null,
                    //加载下一页的key 如果传null就说明到底了
                    null)
            } catch (e: Exception) {
                PagingSource.LoadResult.Error(e)
            }

        }
    }

    val data = pager.getData(viewModelScope)

}