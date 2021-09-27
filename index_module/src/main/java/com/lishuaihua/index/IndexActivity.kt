package com.lishuaihua.index

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.lishuaihua.baselib.base.BaseViewModelActivity
import com.lishuaihua.baselib.binding.ext.viewbind
import com.lishuaihua.baselib.sp.SharedPreferencesManager
import com.lishuaihua.baselib.util.CommonUtil
import com.lishuaihua.baselib.util.StringUtils
import com.lishuaihua.recyclerview.GridDividerItemDecoration
import com.lishuaihua.index.adapter.BottomHolder
import com.lishuaihua.index.adapter.BottomPlaceHolder
import com.lishuaihua.index.databinding.ActivityIndexBinding
import com.lishuaihua.paging3.ext.bind
import com.lishuaihua.paging3.simple.SimplePagingAdapter

@Route(path = "/index/index")
class IndexActivity : BaseViewModelActivity<IndexViewModel>() {
    private val binding: ActivityIndexBinding by viewbind()

    private lateinit var bottomAdapter: SimplePagingAdapter
    override fun getLayoutResId(): Int = R.layout.activity_index

    override fun doCreateView(savedInstanceState: Bundle?) {
        binding.navigationBar.setTitle("paging3")
        binding.navigationBar.setBackClickListener(this)
        val instance = SharedPreferencesManager.getInstance(this)
        binding.et.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s!=null&&!StringUtils.isEmpty(s.toString())&&instance!=null){
                    val keyWord = s.toString()
                    val dataList = instance.getDataList<String>(SharedPreferencesManager.SPCommons.KEY_WORD)
                    val index = dataList.indexOf(keyWord)
                }

            }
        })
        binding.recycleView.layoutManager = GridLayoutManager(this, 2)
        binding.recycleView.addItemDecoration(
            GridDividerItemDecoration(CommonUtil.dp2px(this, 10f))
        )
        bottomAdapter = SimplePagingAdapter(BottomHolder(), BottomPlaceHolder())
        binding.recycleView.setAdapter(bottomAdapter)

        //绑定下拉刷新状态
        bottomAdapter.bind(binding.refreshLayout)
        //绑定数据源
        bottomAdapter.setPager(vm.pager)

    }

}