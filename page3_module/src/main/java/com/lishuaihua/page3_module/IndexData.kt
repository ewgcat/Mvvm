package com.lishuaihua.page3_module

import com.google.gson.annotations.SerializedName

data class IndexData(
    @SerializedName("name")
    var name: String = "",
    @SerializedName("wxMiniHotAreaModule")
    var wxMiniHotAreaModule: String? = null,
    @SerializedName("itemList")
    var itemList: List<ItemListItem>?,
    @SerializedName("sort")
    var sort: Int = 0,
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("type")
    val type: String = "",
    @SerializedName("key")
    val key: String = "",
    @SerializedName("url")
    val url: String? = null,
    @SerializedName("desc")
    val desc: String = ""
)

