package com.lishuaihua.index.navigation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.lishuaihua.index.R
import com.lishuaihua.index.bean.ItemListItem

import java.util.ArrayList

class NavigationAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //图文导航
    private var list: ArrayList<ItemListItem>

    init {
        list = ArrayList<ItemListItem>()
    }


    fun setNavigationList(list: ArrayList<ItemListItem>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = inflate(parent, R.layout.view_navigation)
        return NavigationItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as NavigationItemViewHolder
        viewHolder.bindView(context,list.get(position))
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    private fun inflate(parent: ViewGroup, layoutRes: Int): View {
        return LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
    }





}
