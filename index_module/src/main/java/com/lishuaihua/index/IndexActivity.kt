package com.lishuaihua.index

import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.lishuaihua.baselib.base.BaseActivity
import com.lishuaihua.baselib.binding.binding
import com.lishuaihua.index.bean.ItemListItem
import com.lishuaihua.index.databinding.ActivityIndexBinding
import com.lishuaihua.paging3.SimplePagingAdapter
import com.lishuaihua.paging3.State
import com.lishuaihua.paging3.StatusPager
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.coroutines.flow.collectLatest

class IndexActivity : BaseActivity<IndexViewModel>() {
    private val binding: ActivityIndexBinding by binding()
    private lateinit var customAdapter: CustomAdapter
    private lateinit var adapter : SimplePagingAdapter
    private lateinit var statePager : StatusPager

    override fun getLayoutResId(): Int = R.layout.activity_index

    override fun doCreateView(savedInstanceState: Bundle?) {
        binding.navigationBar.setTitle("paging3 的使用")
        binding.navigationBar.setBackClickListener(this)
        statePager=StatusPager.builder(binding.refreshLayout)
            .emptyViewLayout(R.layout.state_empty)
            .loadingViewLayout(R.layout.state_loading)
            .errorViewLayout(R.layout.state_error)
            .addRetryButtonId(R.id.btn_retry)
            .setRetryClickListener { _, _ ->
                adapter.refresh()
            }
            .build()
        binding.recycleView.layoutManager = GridLayoutManager(this, 2)
        adapter= SimplePagingAdapter(IndexHolder(this,true))
        StatusPager.builder(binding.refreshLayout)
            .emptyViewLayout(R.layout.state_empty)
            .loadingViewLayout(R.layout.state_loading)
            .errorViewLayout(R.layout.state_error)
            .addRetryButtonId(R.id.btn_retry)
            .setRetryClickListener { _, _ ->
                adapter.refresh()
            }
            .build()
        binding.recycleView.adapter = adapter

        binding.refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                adapter.refresh()
            }
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                adapter.retry()
            }
        })
        //绑定数据源
        lifecycleScope.launchWhenCreated {
            vm.data.collectLatest {
                adapter.setData(this@IndexActivity.lifecycle, it)
            }
        }
        //请求状态
        adapter.setOnRefreshStateListener {
            when (it) {
                is State.Loading -> {
                    //如果是手动下拉刷新，则不展示loading页
                    if ( binding.refreshLayout.state != RefreshState.Refreshing) {
                        statePager.showLoading()
                    }
                    binding.refreshLayout.resetNoMoreData()
                }
                is State.Success -> {
                    statePager.showContent()
                    binding.refreshLayout.finishRefresh(true)
                    binding.refreshLayout.setNoMoreData(it.noMoreData)
                }
                is State.Error -> {
                    statePager.showError()
                    binding.refreshLayout.finishRefresh(false)
                }
            }
        }
        //加载更多状态
        adapter.setOnLoadMoreStateListener {
            when (it) {
                is State.Loading -> {
                    //重置上拉加载状态，显示加载loading
                    binding.refreshLayout.resetNoMoreData()
                }
                is State.Success -> {
                    if (it.noMoreData) {
                        //没有更多了(只能用source的append)
                        binding.refreshLayout.finishLoadMoreWithNoMoreData()
                    } else {
                        binding.refreshLayout.finishLoadMore(true)
                    }
                }
                is State.Error -> {
                    binding.refreshLayout.finishLoadMore(false)
                }
            }
        }
        statePager.showLoading()
    }



}