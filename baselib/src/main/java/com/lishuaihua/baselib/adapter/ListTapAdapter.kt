package com.lishuaihua.baselib.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lishuaihua.baselib.R
import com.lishuaihua.baselib.bean.ListTagVO
import java.util.*

class ListTapAdapter(list: List<ListTagVO>?, type: Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    private val list: MutableList<ListTagVO>
    private var type = 0 //如果1//则返回一个，，是品牌下的商品标签
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ListTagHolder(inflate(parent, R.layout.adapter_list_tag))
    }

    private fun inflate(parent: ViewGroup, layoutRes: Int): View {
        return LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val listTagHolder = holder as ListTagHolder
        listTagHolder.tv_tag.setText(list[position].tagValue)
        if (list[position].tagType === 2) { //活动标签，实心
            listTagHolder.tv_tag.setBackgroundResource(R.drawable.gialen_list_tag_red)
            listTagHolder.tv_tag.setTextColor(Color.parseColor("#ffffff"))
        } else if (list[position].tagType === 1) { //日常类标签，空心
            listTagHolder.tv_tag.setBackgroundResource(R.drawable.gialen_list_tag_null_red)
            listTagHolder.tv_tag.setTextColor(Color.parseColor("#ee2532"))
        } else if (list[position].tagType === 3) { //日常类标签，空心
            listTagHolder.tv_tag.setBackgroundResource(R.drawable.gialen_list_tag_null_green)
            listTagHolder.tv_tag.setTextColor(Color.parseColor("#47b286"))
        } else if (list[position].tagType === 4) { //日常类标签，空心
            listTagHolder.tv_tag.setBackgroundResource(R.drawable.gialen_list_tag_blue)
            listTagHolder.tv_tag.setTextColor(Color.parseColor("#ffffff"))
        } else {
            listTagHolder.tv_tag.setBackgroundResource(R.drawable.gialen_list_tag_null_red)
            listTagHolder.tv_tag.setTextColor(Color.parseColor("#ee2532"))
        }
    }

    override fun getItemCount(): Int {
        return if (type == 1) if (list.size > 1) 1 else list.size else list.size
    }

    private inner class ListTagHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_tag: TextView

        init {
            tv_tag = itemView.findViewById(R.id.tv_tag)
        }
    }

    init {
        this.list = ArrayList()
        this.type = type
        if (list != null) {
            this.list.addAll(list)
        }
    }
}
