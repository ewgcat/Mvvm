package com.lishuaihua.recyclerview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AutoLoadRecyclerView  : RecyclerView {
    //内置的HeaderViewAdapter包装对象。
    private var mAdapter: HeaderViewAdapter? = null

    constructor(context: Context?) : super(context!!) {
        wrapHeaderAdapter()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        wrapHeaderAdapter()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context!!, attrs, defStyle) {
        wrapHeaderAdapter()
    }

    override fun setLayoutManager(layout: LayoutManager?) {
        //如果要使用GridLayoutManager的话，只能使用HeaderViewGridLayoutManager。
        if (layout is GridLayoutManager && layout !is HeaderViewGridLayoutManager) {
            super.setLayoutManager(HeaderViewGridLayoutManager(context,
                layout.spanCount, mAdapter))
        } else {
            super.setLayoutManager(layout)
        }
    }

    private fun wrapHeaderAdapter() {
        mAdapter = HeaderViewAdapter(super.getAdapter())
        super.setAdapter(mAdapter)
    }

    override fun setAdapter(adapter: Adapter<RecyclerView.ViewHolder>?) {
        mAdapter!!.adapter = adapter
    }

    override fun getAdapter(): Adapter<*>? {
        return mAdapter!!.adapter
    }

    /**
     * 获取HeaderView的个数
     *
     * @return
     */
    val headersCount: Int
        get() = mAdapter!!.headersCount

    /**
     * 获取FooterView的个数
     *
     * @return
     */
    val footersCount: Int
        get() = mAdapter!!.footersCount

    /**
     * 添加HeaderView
     *
     * @param view
     */
    fun addHeaderView(view: View?) {
        mAdapter!!.addHeaderView(view!!)
    }

    /**
     * 删除HeaderView
     *
     * @param view
     * @return 是否删除成功
     */
    fun removeHeaderView(view: View?): Boolean {
        return mAdapter!!.removeHeaderView(view!!)
    }

    /**
     * 添加FooterView
     *
     * @param view
     */
    fun addFooterView(view: View?) {
        mAdapter!!.addFooterView(view!!)
    }

    /**
     * 删除FooterView
     *
     * @param view
     * @return 是否删除成功
     */
    fun removeFooterView(view: View?): Boolean {
        return mAdapter!!.removeFooterView(view!!)
    }
    private fun init() {
        addOnScrollListener(ImageAutoLoadScrollListener())
    }

    inner class ImageAutoLoadScrollListener :
        OnScrollListener() {
        override fun onScrolled(
            recyclerView: RecyclerView,
            dx: Int,
            dy: Int
        ) {
            super.onScrolled(recyclerView, dx, dy)
        }

        override fun onScrollStateChanged(
            recyclerView: RecyclerView,
            newState: Int
        ) {
            super.onScrollStateChanged(recyclerView, newState)
            when (newState) {
                SCROLL_STATE_IDLE ->
                    try {
                        if (context != null) Glide.with(context).resumeRequests()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                SCROLL_STATE_DRAGGING ->
                    try {
                        if (context != null) Glide.with(context).pauseRequests()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                SCROLL_STATE_SETTLING ->
                    try {
                        if (context != null) Glide.with(context).pauseRequests()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
            }
        }
    }

    init {
        init()
    }
}