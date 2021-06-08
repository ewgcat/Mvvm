package com.lishuaihua.index.adv

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.lishuaihua.baselib.util.CommonUtil
import com.lishuaihua.baselib.util.ImageLoader
import com.lishuaihua.baselib.util.ScreenUtil
import com.lishuaihua.index.R
import com.lishuaihua.index.bean.ItemListItem

import org.json.JSONObject

class AdvItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var iv: ImageView

    init {
        iv = itemView.findViewById(R.id.iv)
    }

    fun bindView(context: Context, itemListBean: ItemListItem) {

        val width = ScreenUtil.getScreenWidth(context)- CommonUtil.dp2px(context,20f)
        val height=width/4
     ImageLoader.setImageResource(context,itemListBean.imgUrl,width,height,iv)

    }
}
