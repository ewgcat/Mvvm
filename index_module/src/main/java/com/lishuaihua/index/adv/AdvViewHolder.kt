package com.lishuaihua.index.adv

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lishuaihua.index.R
import com.lishuaihua.index.bean.IndexData
import com.lishuaihua.index.bean.ItemListItem
import java.util.ArrayList

class AdvViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var recyclerView: RecyclerView

    init {
        recyclerView = itemView.findViewById(R.id.recyclerView)
    }

    fun bindView(context: Context, indexData: IndexData) {
        recyclerView.layoutManager=LinearLayoutManager(context)
        val advAdapter = IndexAdvAdapter(context)
        recyclerView.adapter= advAdapter
        advAdapter.setList(indexData.itemList as ArrayList<ItemListItem>)
    }
}
