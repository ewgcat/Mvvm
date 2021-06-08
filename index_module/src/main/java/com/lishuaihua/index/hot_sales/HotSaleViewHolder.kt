package com.lishuaihua.index.hot_sales

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gialen.baselib.util.*
import com.lishuaihua.baselib.util.CommonUtil
import com.lishuaihua.index.R
import com.lishuaihua.baselib.util.GlideRoundTransform
import com.lishuaihua.baselib.util.ScreenUtil
import com.lishuaihua.index.bean.IndexData
import org.json.JSONObject

class HotSaleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var iv_one: ImageView
    var iv_two: ImageView
    var iv_three: ImageView
    var iv_four: ImageView
    var tv_name: TextView
    var tv_desc: TextView

    init {
        tv_name = itemView.findViewById(R.id.tv_name)
        tv_desc = itemView.findViewById(R.id.tv_desc)
        iv_one = itemView.findViewById(R.id.iv_one)
        iv_two = itemView.findViewById(R.id.iv_two)
        iv_three = itemView.findViewById(R.id.iv_three)
        iv_four = itemView.findViewById(R.id.iv_four)
    }

    fun bindView(context: Context, indexData: IndexData) {
        val width = (ScreenUtil.getScreenWidth(context)- CommonUtil.dp2px(context,80f))/2
        val height=width*2/3
        com.lishuaihua.baselib.util.ImageLoader.setImageResource(context,indexData.itemList.get(0).imgUrl,width,height, GlideRoundTransform(3),iv_one)
        com.lishuaihua.baselib.util.ImageLoader.setImageResource(context,indexData.itemList.get(1).imgUrl,width,height,GlideRoundTransform(3),iv_two)
        com.lishuaihua.baselib.util.ImageLoader.setImageResource(context,indexData.itemList.get(2).imgUrl,width,height,GlideRoundTransform(3),iv_three)
        com.lishuaihua.baselib.util.ImageLoader.setImageResource(context,indexData.itemList.get(3).imgUrl,width,height,GlideRoundTransform(3),iv_four)
        tv_name.text=indexData.name
        tv_desc.text=indexData.desc
    }
}
