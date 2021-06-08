package com.lishuaihua.index.limittime

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.lishuaihua.index.R
import com.lishuaihua.index.bean.ItemListItem

import java.util.ArrayList

class TimeItemAdapter(private val context: Context, private val onClickTimeItem: OnClickTimeItem) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), OnClickTimeItem {
    override fun onClickTime(position: Int) {
        for (i in list.indices) {
            if (i == position) {
                list.get(i)!!.att.checked = true
            } else {
                list.get(i)!!.att.checked = false
            }
        }
        notifyDataSetChanged()
        onClickTimeItem.onClickTime(position)
    }

    //图文导航
    private var list: ArrayList<ItemListItem>

    init {
        list = ArrayList<ItemListItem>()
    }


    fun setList(list: List<ItemListItem>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = inflate(parent, R.layout.view_time)
        return TimeLimitItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as TimeLimitItemViewHolder
        viewHolder.bindView(list, position, this)

    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    private fun inflate(parent: ViewGroup, layoutRes: Int): View {
        return LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
    }


}
