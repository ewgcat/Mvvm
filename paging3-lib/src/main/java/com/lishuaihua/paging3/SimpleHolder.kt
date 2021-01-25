package com.lishuaihua.paging3

import androidx.annotation.LayoutRes


abstract class SimpleHolder<T : DifferData> : PagingDataAdapterKtx.ItemHolder<T>() {

    @LayoutRes
    abstract fun getLayoutRes(): Int

}