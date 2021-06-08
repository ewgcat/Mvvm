package com.lishuaihua.recyclerview

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager


class HeaderViewGridLayoutManager : GridLayoutManager {
    private var mAdapter: HeaderViewAdapter?

    constructor(context: Context?, spanCount: Int, adapter: HeaderViewAdapter?) : super(context, spanCount) {
        mAdapter = adapter
        setSpanSizeLookup()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int,
                defStyleRes: Int, adapter: HeaderViewAdapter?) : super(context, attrs, defStyleAttr, defStyleRes) {
        mAdapter = adapter
        setSpanSizeLookup()
    }

    constructor(context: Context?, spanCount: Int, orientation: Int,
                reverseLayout: Boolean, adapter: HeaderViewAdapter?) : super(context, spanCount, orientation, reverseLayout) {
        mAdapter = adapter
        setSpanSizeLookup()
    }

    private fun setSpanSizeLookup() {
        super.setSpanSizeLookup(object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (mAdapter != null) {
                    if (mAdapter!!.isHeader(position) || mAdapter!!.isFooter(position)) {
                        //如果是HeaderView 或者 FooterView，就让它占满一行。
                        spanCount
                    } else {
                        val adjPosition = position - mAdapter!!.headersCount
                        this@HeaderViewGridLayoutManager.getSpanSize(adjPosition)
                    }
                } else {
                    1
                }
            }
        })
    }

    /**
     * 提供这个方法可以使外部改变普通列表项的SpanSize。
     * 这个方法的作用跟[SpanSizeLookup.getSpanSize]一样。
     *
     * @param position 去掉HeaderView和FooterView后的position。
     * @return
     */
    fun getSpanSize(position: Int): Int {
        return 1
    }

    // 不允许外部设置SpanSizeLookup。
    override fun setSpanSizeLookup(spanSizeLookup: SpanSizeLookup) {}
}