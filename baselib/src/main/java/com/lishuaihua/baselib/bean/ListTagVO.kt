package com.lishuaihua.baselib.bean




class ListTagVO  {
    var tagType //0 新品 1 商品标签 2 活动标签
            : Int = 1
    var tagValue: String = ""

    override fun toString(): String {
        return "ListTagVO{" +
                "tagType='" + tagType + '\'' +
                ", tagValue='" + tagValue + '\'' +
                '}'
    }
}

