package com.lishuaihua.paging3

/**
 * @description : 数据比较类
 */
interface DifferData {

    fun areItemsTheSame(d: DifferData): Boolean {
        return this == d
    }

    fun areContentsTheSame(d: DifferData): Boolean {
        return this == d
    }

    fun getChangePayload(d: DifferData): Any? {
        return null
    }

}