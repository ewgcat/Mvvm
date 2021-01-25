package com.lishuaihua.paging3.simple

import android.view.View
import androidx.annotation.LayoutRes
import com.lishuaihua.paging3.adapter.DifferData
import com.lishuaihua.paging3.adapter.ItemHelper
import com.lishuaihua.paging3.adapter.ItemHolder
import kotlinx.android.extensions.LayoutContainer

/**
 * @description : 简易holder
 */
abstract class SimpleHolder<T : DifferData>(@LayoutRes val res: Int) : ItemHolder<T>() {

    @LayoutRes
    fun getLayoutRes(): Int = res

    final override fun bindData(
        item: ItemHelper,
        data: T?,
        payloads: MutableList<Any>?
    ) {
        if (data == null) return //简易holder data不为空
        bindItem(item, data, payloads)
    }

    abstract fun bindItem(
        item: ItemHelper,
        data: T,
        payloads: MutableList<Any>?
    )

}