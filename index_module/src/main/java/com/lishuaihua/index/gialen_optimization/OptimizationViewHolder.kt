package com.lishuaihua.index.gialen_optimization

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lishuaihua.index.R
import com.lishuaihua.index.bean.IndexData
import com.lishuaihua.index.bean.ItemListItem
import java.util.ArrayList

class OptimizationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var recycler_view: RecyclerView
    var tv_name: TextView
    var tv_desc: TextView

    init {
        recycler_view = itemView.findViewById(R.id.recycler_view)
        tv_name = itemView.findViewById(R.id.tv_name)
        tv_desc = itemView.findViewById(R.id.tv_desc)
    }

    fun bindView(context: Context, indexData: IndexData, isVip:Boolean) {
        tv_name.text=indexData.name
        tv_desc.text=indexData.desc
        recycler_view.layoutManager=LinearLayoutManager(context)
        var adapter =OptimizationAdapter(context,isVip )
        recycler_view.adapter=adapter
        val itemList = indexData.itemList
        if (itemList!=null&&!itemList.isEmpty()){
            adapter.setList(itemList as ArrayList<ItemListItem>)
        }
    }
}
