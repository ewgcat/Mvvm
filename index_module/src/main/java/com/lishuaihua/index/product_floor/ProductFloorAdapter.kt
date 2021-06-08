package com.lishuaihua.index.product_floor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lishuaihua.index.R
import com.lishuaihua.index.bean.ItemListItem
import java.util.ArrayList

class ProductFloorAdapter(private val context: Context,val isVip:Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
        val view = inflate(parent, R.layout.view_product_floor)
        return ProductFloorItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ProductFloorItemViewHolder
        viewHolder.bindView(context,list.get(position),isVip)
    }

    override fun getItemCount(): Int {
        val size = list!!.size
        return size
    }

    private fun inflate(parent: ViewGroup, layoutRes: Int): View {
        return LayoutInflater.from(parent.context).inflate(layoutRes, null, false)
    }

}
