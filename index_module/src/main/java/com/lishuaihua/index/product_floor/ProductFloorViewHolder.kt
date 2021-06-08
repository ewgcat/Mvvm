package com.lishuaihua.index.product_floor

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lishuaihua.index.R
import com.lishuaihua.index.bean.IndexData
import com.lishuaihua.index.bean.ItemListItem
import java.util.ArrayList

class ProductFloorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    var recycler_view: RecyclerView


    init {
        recycler_view = itemView.findViewById(R.id.recycler_view)

    }

    fun bindView(context: Context, indexData: IndexData, isVip:Boolean) {

        recycler_view.layoutManager= LinearLayoutManager(context)
        var adapter = ProductFloorAdapter(context,isVip)
        recycler_view.adapter=adapter
        val itemList = indexData.itemList
        if (itemList!=null&&!itemList.isEmpty()){
            adapter.setList(itemList as ArrayList<ItemListItem>)
        }
    }
}