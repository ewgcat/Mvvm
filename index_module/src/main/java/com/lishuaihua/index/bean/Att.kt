package com.lishuaihua.index.bean

import com.google.gson.annotations.SerializedName

data class Att(
    @SerializedName("checked")
    var checked: Boolean = false,
    @SerializedName("status")
    var status: String = "",
    @SerializedName("color")
    var color: String = "#ffffff",
    @SerializedName("statusDesc")
    var statusDesc: String = "#ffffff",
    @SerializedName("marketPrice")
    var marketPrice: Double? = 0.0,
    @SerializedName("salePrice")
    var salePrice: Double? = 0.0,
    @SerializedName("commission")
    var commission: Double? = 0.0,
    @SerializedName("tags")
    var tags: List<TagsItem>
)
