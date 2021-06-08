package com.lishuaihua.index.limittime

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lishuaihua.index.R
import com.lishuaihua.index.bean.ItemListItem
import java.util.ArrayList

class TimeLimitItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var iv_time_select: ImageView
    var tv_sub_title: TextView
    var tv_status: TextView


    init {
        iv_time_select = itemView.findViewById(R.id.iv_time_select)
        tv_sub_title = itemView.findViewById(R.id.tv_sub_title)
        tv_status = itemView.findViewById(R.id.tv_status)

    }

    fun bindView(list: ArrayList<ItemListItem>, position: Int, onClickTimeItem: OnClickTimeItem) {
        var itemListBean = list.get(position)
        if (itemListBean!!.att.checked != null && itemListBean!!.att.checked) {
            iv_time_select.visibility = View.VISIBLE
            tv_sub_title.text = itemListBean!!.title
            tv_status.text = itemListBean!!.att.statusDesc
            tv_sub_title.setTextColor(Color.parseColor("#212121"))
            tv_status.setTextColor(Color.parseColor("#212121"))

        } else {
            iv_time_select.visibility = View.INVISIBLE
            tv_sub_title.text = itemListBean!!.title
            tv_status.text = itemListBean!!.att.statusDesc
            tv_sub_title.setTextColor(Color.parseColor("#888888"))
            tv_status.setTextColor(Color.parseColor("#888888"))
        }
        itemView.setOnClickListener {
            onClickTimeItem.onClickTime(position)
        }

    }

}