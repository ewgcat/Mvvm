package com.lishuaihua.index

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.lishuaihua.baselib.base.BaseActivity
import com.lishuaihua.baselib.binding.binding
import com.lishuaihua.index.adapter.IndexHolder
import com.lishuaihua.index.adapter.PlaceHolder
import com.lishuaihua.index.databinding.ActivityIndexBinding
import com.lishuaihua.paging3.ext.bind
import com.lishuaihua.paging3.simple.SimplePagingAdapter

class IndexActivity : BaseActivity<IndexViewModel>() {
    private val binding: ActivityIndexBinding by binding()
    private lateinit var adapter : SimplePagingAdapter

    override fun getLayoutResId(): Int = R.layout.activity_index

    override fun doCreateView(savedInstanceState: Bundle?) {
        initRv()

    }

    private fun initRv() {
        binding.navigationBar.setTitle("paging3 的使用")
        binding.navigationBar.setBackClickListener(this)

        binding.recycleView.layoutManager = GridLayoutManager(this, 2)
        adapter = SimplePagingAdapter(IndexHolder(), PlaceHolder())

        binding.recycleView.adapter = adapter
        //绑定下拉刷新状态
        adapter.bind(binding.refreshLayout)
        //绑定数据源
        adapter.setPager(vm.pager)
    }


}