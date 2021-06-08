package com.lishuaihua.index.product_floor

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lishuaihua.baselib.util.CommonUtil
import com.lishuaihua.baselib.util.ScreenUtil
import com.lishuaihua.baselib.util.StringUtils
import com.lishuaihua.index.R
import com.lishuaihua.index.bean.ItemListItem
import com.lishuaihua.index.bean.SecondItemListBean
import org.json.JSONObject
import java.util.ArrayList

class ProductFloorItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var recyclerView: RecyclerView
    var iv: ImageView
    var ll_all: LinearLayout

    init {
        iv = itemView.findViewById(R.id.iv)
        ll_all = itemView.findViewById(R.id.ll_all)
        recyclerView = itemView.findViewById(R.id.recyclerView)
    }

    fun bindView(context: Context, itemListBean: ItemListItem, isVip:Boolean) {
        if(itemListBean.att!=null&& !StringUtils.isEmpty(itemListBean.att.color)){
            ll_all.setBackgroundColor(Color.parseColor(itemListBean.att.color))
        }
        if (itemListBean.imgWidth!=0){
            val width = ScreenUtil.getScreenWidth(context)- CommonUtil.dp2px(context,20f)
            val height=itemListBean.imgHeight*width/itemListBean.imgWidth
            com.lishuaihua.baselib.util.ImageLoader.setImageResource(context,itemListBean.imgUrl, R.mipmap.ic_default_logo,width,height,iv)
        }else{
            com.lishuaihua.baselib.util.ImageLoader.setImageResource(context,itemListBean.imgUrl, iv)
        }

        recyclerView.layoutManager= GridLayoutManager(context,3) as RecyclerView.LayoutManager
        var productFloorGoodsAdapter = ProductFloorGoodsAdapter(context,isVip)
        recyclerView.adapter=productFloorGoodsAdapter
        val itemList = itemListBean.itemList
        if (itemList!=null&&!itemList.isEmpty()){
            productFloorGoodsAdapter.setList(itemList as ArrayList<SecondItemListBean>)
            productFloorGoodsAdapter.notifyDataSetChanged()
        }
    }
}