package com.lishuaihua.page3_module

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.lishuaihua.baselib.util.MD5Util
import com.lishuaihua.net.httputils.BaseViewModel
import com.lishuaihua.net.httputils.HttpUtils
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


}