package com.lishuaihua.index.navigation

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lishuaihua.index.R
import com.lishuaihua.baselib.util.ImageLoader
import com.lishuaihua.index.bean.ItemListItem
import org.json.JSONObject

class NavigationItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var iv: ImageView
    var tv_title: TextView


    init {
        iv = itemView.findViewById(R.id.iv)
        tv_title = itemView.findViewById(R.id.tv_title)
    }

    fun bindView(context: Context, itemListBean: ItemListItem) {
        ImageLoader.setImageResource(context,itemListBean.imgUrl,true,iv)
        tv_title.text=itemListBean.title

    }
}
