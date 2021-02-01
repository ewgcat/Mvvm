package com.lishuaihua.paging3.ext

import android.content.Context
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.lishuaihua.paging3.tools.StringData


/**
 * 线性列表指定条目滑动到顶部第一个可见位置
 */
fun RecyclerView.smoothScrollToPositionWithTop(position: Int) {
    val topScroller = TopSmoothScroller(context)
    topScroller.targetPosition = position
    layoutManager?.startSmoothScroll(topScroller)
}

/**
 * 自定义线性滑动器
 */
class TopSmoothScroller(context: Context) : LinearSmoothScroller(context) {
    override fun getHorizontalSnapPreference(): Int {
        return SNAP_TO_START
    }

    override fun getVerticalSnapPreference(): Int {
        return SNAP_TO_START
    }
}

/**
 * 文字数组转StringData列表
 */
fun Array<out CharSequence>.toStringDataList() =
    this.map { StringData(it) }

/**
 * 文字列表转StringData列表
 */
fun List<CharSequence>.toStringDataList() =
    this.map { StringData(it) }
