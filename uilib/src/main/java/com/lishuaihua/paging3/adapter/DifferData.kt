package com.lishuaihua.paging3.adapter

/**
 * @description : 数据比较接口
 */
interface DifferData {

    fun areItemsTheSame(data: DifferData): Boolean {
        return this == data
    }

    fun areContentsTheSame(data: DifferData): Boolean {
        return this == data
    }

    fun getChangePayload(data: DifferData): Any? {
        return null
    }

}