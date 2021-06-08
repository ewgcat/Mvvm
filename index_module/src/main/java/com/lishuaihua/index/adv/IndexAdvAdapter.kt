package com.lishuaihua.index.adv

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.lishuaihua.index.R
import com.lishuaihua.index.bean.ItemListItem

import java.util.ArrayList

class IndexAdvAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: ArrayList<ItemListItem>

    init {
        list = ArrayList<ItemListItem>()
    }


    fun setList(list: ArrayList<ItemListItem>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = inflate(parent, R.layout.view_adv)
        return AdvItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as AdvItemViewHolder
        viewHolder.bindView(context,list.get(position))
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    private fun inflate(parent: ViewGroup, layoutRes: Int): View {
        return LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
    }





}
