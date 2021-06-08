package com.lishuaihua.index.gialen_optimization

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.lishuaihua.index.R
import com.lishuaihua.index.bean.SecondItemListBean

import java.util.ArrayList

class OptimizationGoodsAdapter(private val context: Context,val isVip:Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: ArrayList<SecondItemListBean>

    init {
        list = ArrayList<SecondItemListBean>()
    }


    fun setList(list: ArrayList<SecondItemListBean>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = inflate(parent, R.layout.view_optimization_gialen_goods)
        return GialenGoodsViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as GialenGoodsViewHolder
        viewHolder.bindView(context,list.get(position),isVip)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    private fun inflate(parent: ViewGroup, layoutRes: Int): View {
        return LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
    }





}
