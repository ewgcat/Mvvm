package com.lishuaihua.index.bean

import com.google.gson.annotations.SerializedName

data class IndexData(
    @SerializedName("name")
    var name: String = "",
    @SerializedName("wxMiniHotAreaModule")
    var wxMiniHotAreaModule: String? = null,
    @SerializedName("itemList")
    var itemList: List<ItemListItem> = emptyList(),
    @SerializedName("sort")
    var sort: Int = 0,
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("type")
    val type: String = "",
    @SerializedName("key")
    val key: String = "",
    @SerializedName("url")
    val url: String = "",
    @SerializedName("desc")
    val desc: String = "",

    val totalCount: Int = 10000,
)

