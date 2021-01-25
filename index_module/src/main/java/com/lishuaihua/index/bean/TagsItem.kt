package com.lishuaihua.index.bean

import com.google.gson.annotations.SerializedName

data class TagsItem(@SerializedName("tagValue")
                    val tagValue: String = "",
                    @SerializedName("tagType")
                    val tagType: Int = 0)