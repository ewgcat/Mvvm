package com.lishuaihua.index.limittime

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lishuaihua.index.R
import com.lishuaihua.index.bean.IndexData
import com.lishuaihua.index.bean.ItemListItem
import com.lishuaihua.index.bean.SecondItemListBean
import java.util.ArrayList

class TimeLimitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), OnClickTimeItem {
    override fun onClickTime(position: Int) {
        timeAdapter.notifyDataSetChanged()
        timeProductAdapter.setList(itemList.get(position).itemList as  ArrayList<SecondItemListBean> )
    }


    var recyclerView: RecyclerView
    var recycler_view: RecyclerView

    lateinit var timeProductAdapter: TimeProductAdapter
    lateinit var timeAdapter: TimeItemAdapter
    lateinit var itemList:ArrayList<ItemListItem>


    init {
        recyclerView = itemView.findViewById(R.id.recyclerView)
        recycler_view = itemView.findViewById(R.id.recycler_view)

    }

    fun bindView(context: Context, indexData: IndexData, isVip:Boolean) {
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false) as RecyclerView.LayoutManager

        timeAdapter = TimeItemAdapter(context,this )
        recyclerView.adapter = timeAdapter
        timeAdapter.setList(indexData.itemList)

        recycler_view.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false) as RecyclerView.LayoutManager
        timeProductAdapter = TimeProductAdapter(context,isVip)
        recycler_view.adapter = timeProductAdapter
         itemList = indexData.itemList as ArrayList<ItemListItem>
        if (itemList!=null&&itemList.isNotEmpty()){
            for(item in itemList){
                if (item.att.checked != null&&item.att.checked){
                    timeProductAdapter.setList(item.itemList as ArrayList<SecondItemListBean>)
                }
            }
        }



    }

}