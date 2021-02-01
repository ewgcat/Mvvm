package com.lishuaihua.paging3.simple

import androidx.annotation.LayoutRes
import com.lishuaihua.paging3.adapter.DifferData
import com.lishuaihua.paging3.adapter.ItemHelper

/**
 * @description : paging 占位holder
 * 当数据为空的时候 显示的占位holder，用来确认固定数量条目,以及展示占位图
 */
open class SimplePlaceHolder(@LayoutRes val layout: Int) :
    SimpleHolder<DifferData>(layout) {

    override fun bindItem(
        item: ItemHelper,
        data: DifferData,
        payloads: MutableList<Any>?
    ) {

    }

}