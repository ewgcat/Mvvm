package com.lishuaihua.page3_module

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.lishuaihua.baselib.base.BaseActivity
import com.lishuaihua.baselib.binding.binding
import com.lishuaihua.page3_module.databinding.ActivityIndexBinding
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class IndexActivity : BaseActivity<IndexViewModel>() {
    private val binding: ActivityIndexBinding by binding()
    private lateinit var customAdapter: CustomAdapter
    var page:Int=1
    var pageSize:Int=10
    override fun getLayoutResId(): Int = R.layout.activity_index

    override fun doCreateView(savedInstanceState: Bundle?) {
        binding.navigationBar.setTitle("paging3 的使用")
        binding.navigationBar.setBackClickListener(this)
        binding.recycleView.layoutManager = GridLayoutManager(this, 2)
        customAdapter = CustomAdapter(this)
        binding.recycleView.adapter = customAdapter
        vm.indexLiveData.observe(this,{
            val list = it.itemList
            if (list!=null&&list.isNotEmpty()){
                GlobalScope.launch { customAdapter.submitData(PagingData.from(list)) }
            }
        })
        binding.refreshLayout.setRefreshHeader(ClassicsHeader(this).setAccentColor(Color.parseColor("#ffffff")))
        binding.refreshLayout.setRefreshFooter(ClassicsFooter(this))
        binding.refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                refresh()
            }
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                loadMore()
            }
        })
        vm.isShowLoading.observe(this,  {
            if (it){ showLoading()} else{
                dismissLoding()
                if (page==1){
                    binding.refreshLayout.finishRefresh()
                }else{
                    binding.refreshLayout.finishLoadMore(200, true, false)
                }

            }

        })
        vm.errorData.observe(this,  {
            if (page==1){
                binding.refreshLayout.finishRefresh()
            }else{
                binding.refreshLayout.finishLoadMore(false)
            }
            Toast.makeText(this,it.msg, Toast.LENGTH_SHORT).show()
        })
        refresh()
    }

    fun refresh() {
        page=1
        vm.index(page,pageSize)
    }
     fun loadMore() {
         page++
         vm.index(page,pageSize)
    }

}