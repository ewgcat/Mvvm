package com.lishuaihua.index.navigation

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lishuaihua.index.R
import com.lishuaihua.index.bean.IndexData
import com.lishuaihua.index.bean.ItemListItem
import java.util.ArrayList

class NavigationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var navigation_recyclerview: RecyclerView


    init {
        navigation_recyclerview = itemView.findViewById(R.id.navigation_recyclerview)
    }

    fun bindView(context: Context, indexData: IndexData) {
        navigation_recyclerview.layoutManager=GridLayoutManager(context,4) as RecyclerView.LayoutManager
        val navigationAdapter = NavigationAdapter(context)
        navigation_recyclerview.adapter= navigationAdapter
        navigationAdapter.setNavigationList(indexData.itemList as ArrayList<ItemListItem>)
    }
}
