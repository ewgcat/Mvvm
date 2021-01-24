package com.lishuaihua.page3_module

import com.google.gson.annotations.SerializedName

data class Att(@SerializedName("marketPrice")
               var marketPrice: Double? = 0.0,
               @SerializedName("salePrice")
               var salePrice: Double? = 0.0,
               @SerializedName("commission")
               var commission: Double? = 0.0,
               @SerializedName("tags")
               var tags: List<TagsItem>)