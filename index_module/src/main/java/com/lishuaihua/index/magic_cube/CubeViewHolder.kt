package com.lishuaihua.index.magic_cube

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.lishuaihua.index.R
import com.gialen.baselib.util.*
import com.lishuaihua.baselib.util.CommonUtil
import com.lishuaihua.baselib.util.ScreenUtil
import com.lishuaihua.index.bean.IndexData
import org.json.JSONObject

class CubeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var iv_one: ImageView
    var iv_two: ImageView
    var iv_three: ImageView

    init {
        iv_one = itemView.findViewById(R.id.iv_one)
        iv_two = itemView.findViewById(R.id.iv_two)
        iv_three = itemView.findViewById(R.id.iv_three)
    }

    fun bindView(context: Context, indexData: IndexData) {
        val width = (ScreenUtil.getScreenWidth(context)- CommonUtil.dp2px(context,20.5f))/2
        val height=width
        com.lishuaihua.baselib.util.ImageLoader.setImageResource(context,indexData.itemList.get(0).imgUrl,R.mipmap.ic_default_logo,width,height,iv_one)

        val twoWidth=width
        val twoheight=height/2
        com.lishuaihua.baselib.util.ImageLoader.setImageResource(context,indexData.itemList.get(1).imgUrl,R.mipmap.ic_default_logo,twoWidth,twoheight,iv_two)
        com.lishuaihua.baselib.util.ImageLoader.setImageResource(context,indexData.itemList.get(2).imgUrl,R.mipmap.ic_default_logo,twoWidth,twoheight,iv_three)


    }
}
