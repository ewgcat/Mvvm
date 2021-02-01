package com.lishuaihua.paging3.tools

import com.lishuaihua.paging3.adapter.DifferData

/**
 * @author : leo
 * @date : 2020/11/27
 * @description : 字符串数据
 */
data class StringData(
    val string: CharSequence
) : DifferData {
    override fun areItemsTheSame(data: DifferData): Boolean {
        return (data as? StringData)?.string == string
    }

    override fun areContentsTheSame(data: DifferData): Boolean {
        return (data as? StringData)?.string == string
    }

    override fun toString(): String {
        return string.toString()
    }
}