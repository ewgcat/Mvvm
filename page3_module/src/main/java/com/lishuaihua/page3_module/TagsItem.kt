package com.lishuaihua.page3_module

import com.google.gson.annotations.SerializedName

data class TagsItem(@SerializedName("tagValue")
                    val tagValue: String = "",
                    @SerializedName("tagType")
                    val tagType: Int = 0)