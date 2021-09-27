package com.lishuaihua.index

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Route
import com.lishuaihua.baselib.base.BaseViewModelActivity
import com.lishuaihua.baselib.binding.ext.viewbind
import com.lishuaihua.baselib.util.CommonUtil
import com.lishuaihua.baselib.util.Logger
import com.lishuaihua.index.adapter.BottomHolder
import com.lishuaihua.index.adapter.BottomPlaceHolder
import com.lishuaihua.index.adapter.HeaderAdapter
import com.lishuaihua.index.bean.IndexData
import com.lishuaihua.index.databinding.ActivityHeaderRecyclerViewBinding
import com.lishuaihua.paging3.ext.bind
import com.lishuaihua.paging3.simple.SimplePagingAdapter
import com.lishuaihua.recyclerview.GridDividerItemDecoration
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

@Route(path = "/index/header")
class HeaderRecyclerViewActivity : BaseViewModelActivity<IndexViewModel>() {
    private val binding: ActivityHeaderRecyclerViewBinding by viewbind()

    private lateinit var headerAdapter: HeaderAdapter
    private lateinit var bottomAdapter: SimplePagingAdapter
    var firstPosition = 0
    var currentPosition = 0
    var isVip = true
    lateinit var recyclerView: RecyclerView
    var headerIndexDataList: ArrayList<IndexData> = ArrayList<IndexData>()

    override fun getLayoutResId(): Int = R.layout.activity_header_recycler_view

    override fun doCreateView(savedInstanceState: Bundle?) {
        binding.navigationBar.setTitle("首页")
        binding.navigationBar.setBackClickListener(this)
        binding.recycleView.layoutManager = LinearLayoutManager(HeaderRecyclerViewActivity@ this)
        headerAdapter = HeaderAdapter(
            HeaderRecyclerViewActivity@ this,
            object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    if (firstPosition == 0) {
                        val indexData = headerIndexDataList.get(0)
                        val itemList = indexData.itemList
                        currentPosition = position
                        val color = itemList.get(position).att.color
                        if (!TextUtils.isEmpty(color)) {
                            binding.topView.setBackgroundColor(Color.parseColor(color))
                        }
                    } else {
                        binding.topView.setBackgroundColor(Color.parseColor("#212121"))
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {

                }

            },
            false
        )
        binding.recycleView.setAdapter(headerAdapter)
        vm.getIndexData()

        var footView = LayoutInflater.from(HeaderRecyclerViewActivity@ this).inflate(R.layout.view_item_hotrecomend, null, false)
        recyclerView = footView.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        bottomAdapter = SimplePagingAdapter(BottomHolder(), BottomPlaceHolder())
        recyclerView.addItemDecoration(GridDividerItemDecoration(CommonUtil.dp2px(this, 10f)))
        recyclerView.adapter = bottomAdapter
//        绑定下拉刷新状态
        bottomAdapter.bind(binding.refreshLayout)
//        绑定数据源
        bottomAdapter.setPager(vm.pager)
        binding.recycleView.addFooterView(footView)
    
        vm.indexDataList.observe(this, {
            binding.refreshLayout.finishRefresh(true)
            headerAdapter.setIndexDataList(it)
        })
        vm.errorLiveData.observe(this, {
            Logger.d("error", "error:" + it.msg)
            Toast.makeText(HeaderRecyclerViewActivity@ this, "error:" + it.msg, Toast.LENGTH_SHORT)
                .show()
        })
        binding.refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                vm.getIndexData()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {

            }
        })

    }
}