package com.lishuaihua.index.gialen_optimization

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lishuaihua.baselib.util.CommonUtil
import com.lishuaihua.index.R
import com.lishuaihua.baselib.util.ImageLoader
import com.lishuaihua.baselib.util.ScreenUtil
import com.lishuaihua.index.bean.ItemListItem
import com.lishuaihua.index.bean.SecondItemListBean
import org.json.JSONObject
import java.util.ArrayList

class OptimizationItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var recyclerView: RecyclerView
    var iv: ImageView

    init {
        iv = itemView.findViewById(R.id.iv)
        recyclerView = itemView.findViewById(R.id.recyclerView)
    }

    fun bindView(context: Context, itemListBean: ItemListItem, isVip:Boolean) {
        val width = ScreenUtil.getScreenWidth(context)- CommonUtil.dp2px(context,20f)
        val height=width/2
        ImageLoader.setImageResource(context,itemListBean.imgUrl,R.mipmap.ic_default_logo,width,height,iv)
        recyclerView.layoutManager=GridLayoutManager(context,3) as RecyclerView.LayoutManager
        var optimizationGoodsAdapter = OptimizationGoodsAdapter(context,isVip)
        recyclerView.adapter=optimizationGoodsAdapter
        val itemList = itemListBean.itemList
        if (itemList!=null&&!itemList.isEmpty()){
            optimizationGoodsAdapter.setList(itemList as ArrayList<SecondItemListBean>)
            optimizationGoodsAdapter.notifyDataSetChanged()
        }
    }
}
