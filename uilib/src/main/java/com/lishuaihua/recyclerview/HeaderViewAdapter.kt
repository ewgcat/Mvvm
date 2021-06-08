package com.lishuaihua.recyclerview

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import java.util.ArrayList

class HeaderViewAdapter(  //被包装的Adapter。
        private var mAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        //用于存放HeaderView
        private val mHeaderViewInfos: MutableList<FixedViewInfo> = ArrayList()

        //用于存放FooterView
        private val mFooterViewInfos: MutableList<FixedViewInfo> = ArrayList()

        //用于监听被包装的Adapter的数据变化的监听器。它将被包装的Adapter的数据变化映射成HeaderViewAdapter的变化。
        private val mObserver: RecyclerView.AdapterDataObserver = object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                notifyDataSetChanged()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                super.onItemRangeChanged(positionStart, itemCount)
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                notifyItemRangeChanged(headersCount + positionStart, itemCount, payload)
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                notifyItemRangeInserted(headersCount + positionStart, itemCount)
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                notifyItemRangeRemoved(headersCount + positionStart, itemCount)
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                notifyItemMoved(headersCount + fromPosition, headersCount + toPosition)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            // 根据viewType查找对应的HeaderView 或 FooterView。如果没有找到则表示该viewType是普通的列表项。
            val view = findViewForInfos(viewType)
            return if (view != null) {
                ViewHolder(view)
            } else {
                //交由mAdapter处理。
                mAdapter!!.onCreateViewHolder(parent, viewType)
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            // 如果是HeaderView 或者是 FooterView则不绑定数据。
            // 因为HeaderView和FooterView是由外部传进来的，它们不由列表去更新。
            if (isHeader(position) || isFooter(position)) {
                return
            }

            //将列表实际的position调整成mAdapter对应的position。
            //交由mAdapter处理。
            val adjPosition = position - headersCount
            mAdapter!!.onBindViewHolder(holder, adjPosition)
        }

        override fun getItemCount(): Int {
            return (mHeaderViewInfos.size + mFooterViewInfos.size
                    + if (mAdapter == null) 0 else mAdapter!!.itemCount)
        }

        override fun getItemViewType(position: Int): Int {
            //如果当前item是HeaderView，则返回HeaderView对应的itemViewType。
            if (isHeader(position)) {
                return mHeaderViewInfos[position].itemViewType
            }

            //如果当前item是HeaderView，则返回HeaderView对应的itemViewType。
            if (isFooter(position)) {
                return mFooterViewInfos[position - mHeaderViewInfos.size - mAdapter!!.itemCount].itemViewType
            }

            //将列表实际的position调整成mAdapter对应的position。
            //交由mAdapter处理。
            val adjPosition = position - headersCount
            return mAdapter!!.getItemViewType(adjPosition)
        }
        /**
         * 获取被包装的adapter
         *
         * @return
         *///注册mAdapter的数据变化监听//被包装的adapter不能是HeaderViewAdapter。
        /**
         * 设置被包装的adapter。同一个adapter对象不能设置多次。
         *
         * @param adapter
         */
        var adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>?
            get() = mAdapter
            set(adapter) {
                require(adapter !is HeaderViewAdapter) {
                    //被包装的adapter不能是HeaderViewAdapter。
                    "Cannot wrap a HeaderViewAdapter"
                }
                mAdapter = adapter
                if (mAdapter != null) {
                    //注册mAdapter的数据变化监听
                    mAdapter!!.registerAdapterDataObserver(mObserver)
                }
                notifyDataSetChanged()
            }

        /**
         * 判断当前位置是否是头部View。
         *
         * @param position 这里的position是整个列表(包含HeaderView和FooterView)的position。
         * @return
         */
        fun isHeader(position: Int): Boolean {
            return position < headersCount
        }

        /**
         * 判断当前位置是否是尾部View。
         *
         * @param position 这里的position是整个列表(包含HeaderView和FooterView)的position。
         * @return
         */
        fun isFooter(position: Int): Boolean {
            return itemCount - position <= footersCount
        }

        /**
         * 获取HeaderView的个数
         *
         * @return
         */
        val headersCount: Int
            get() = mHeaderViewInfos.size

        /**
         * 获取FooterView的个数
         *
         * @return
         */
        val footersCount: Int
            get() = mFooterViewInfos.size

        /**
         * 添加HeaderView
         *
         * @param view
         */
        fun addHeaderView(view: View) {
            addHeaderView(view, generateUniqueViewType())
        }

        private fun addHeaderView(view: View, viewType: Int) {
            //包装HeaderView数据并添加到列表
            val info: FixedViewInfo = FixedViewInfo()
            info.view = view
            info.itemViewType = viewType
            mHeaderViewInfos.add(info)
            notifyDataSetChanged()
        }

        /**
         * 删除HeaderView
         *
         * @param view
         * @return 是否删除成功
         */
        fun removeHeaderView(view: View): Boolean {
            for (info in mHeaderViewInfos) {
                if (info.view === view) {
                    mHeaderViewInfos.remove(info)
                    notifyDataSetChanged()
                    return true
                }
            }
            return false
        }

        /**
         * 添加FooterView
         *
         * @param view
         */
        fun addFooterView(view: View) {
            addFooterView(view, generateUniqueViewType())
        }

        private fun addFooterView(view: View, viewType: Int) {
            // 包装FooterView数据并添加到列表
            val info: FixedViewInfo = FixedViewInfo()
            info.view = view
            info.itemViewType = viewType
            mFooterViewInfos.add(info)
            notifyDataSetChanged()
        }

        /**
         * 删除FooterView
         *
         * @param view
         * @return 是否删除成功
         */
        fun removeFooterView(view: View): Boolean {
            for (info in mFooterViewInfos) {
                if (info.view === view) {
                    mFooterViewInfos.remove(info)
                    notifyDataSetChanged()
                    return true
                }
            }
            return false
        }

        /**
         * 生成一个唯一的数，用于标识HeaderView或FooterView的type类型，并且保证类型不会重复。
         *
         * @return
         */
        private fun generateUniqueViewType(): Int {
            val count = itemCount
            while (true) {
                //生成一个随机数。
                val viewType = (Math.random() * Int.MAX_VALUE).toInt() + 1

                //判断该viewType是否已使用。
                var isExist = false
                for (i in 0 until count) {
                    if (viewType == getItemViewType(i)) {
                        isExist = true
                        break
                    }
                }

                //判断该viewType还没被使用，则返回。否则进行下一次循环，重新生成随机数。
                if (!isExist) {
                    return viewType
                }
            }
        }

        /**
         * 根据viewType查找对应的HeaderView 或 FooterView。没有找到则返回null。
         *
         * @param viewType 查找的viewType
         * @return
         */
        private fun findViewForInfos(viewType: Int): View? {
            for (info in mHeaderViewInfos) {
                if (info.itemViewType == viewType) {
                    return info.view
                }
            }
            for (info in mFooterViewInfos) {
                if (info.itemViewType == viewType) {
                    return info.view
                }
            }
            return null
        }

        override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
            if (holder is ViewHolder) {
                super.onViewAttachedToWindow(holder)
            } else {
                mAdapter!!.onViewAttachedToWindow(holder)
            }

            //处理StaggeredGridLayout，保证HeaderView和FooterView占满一行。
            if (isStaggeredGridLayout(holder)) {
                handleLayoutIfStaggeredGridLayout(holder, holder.layoutPosition)
            }
        }

        private fun isStaggeredGridLayout(holder: RecyclerView.ViewHolder): Boolean {
            val layoutParams = holder.itemView.layoutParams
            return if (layoutParams != null && layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                true
            } else false
        }

        private fun handleLayoutIfStaggeredGridLayout(holder: RecyclerView.ViewHolder, position: Int) {
            if (isHeader(position) || isFooter(position)) {
                val p = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
                p.isFullSpan = true
            }
        }

        override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
            if (holder is ViewHolder) {
                super.onViewDetachedFromWindow(holder)
            } else {
                mAdapter!!.onViewDetachedFromWindow(holder)
            }
        }

        override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
            return if (holder is ViewHolder) {
                super.onFailedToRecycleView(holder)
            } else {
                mAdapter!!.onFailedToRecycleView(holder)
            }
        }

        override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
            if (mAdapter != null) {
                mAdapter!!.onAttachedToRecyclerView(recyclerView)
            }
        }

        override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
            if (mAdapter != null) {
                mAdapter!!.onDetachedFromRecyclerView(recyclerView)
            }
        }

        override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
            if (holder is ViewHolder) {
                super.onViewRecycled(holder)
            } else {
                mAdapter!!.onViewRecycled(holder)
            }
        }

        /**
         * 用于包装HeaderView和FooterView的数据类
         */
        private inner class FixedViewInfo {
            //保存HeaderView或FooterView
            var view: View? = null

            //保存HeaderView或FooterView对应的viewType。
            var itemViewType = 0
        }

        private class ViewHolder internal constructor(itemView: View?) : RecyclerView.ViewHolder(itemView!!)

        init {
            if (mAdapter != null) {
                //注册mAdapter的数据变化监听
                mAdapter!!.registerAdapterDataObserver(mObserver)
            }
        }
    }