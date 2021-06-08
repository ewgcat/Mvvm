package com.lishuaihua.index.gialen_optimization

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.gialen.baselib.util.*
import com.lishuaihua.baselib.bean.ListTagVO
import com.lishuaihua.baselib.util.CommonUtil
import com.lishuaihua.baselib.util.ScreenUtil
import com.lishuaihua.baselib.util.StringUtils
import com.lishuaihua.baselib.util.TagUtils
import com.lishuaihua.index.R
import com.lishuaihua.baselib.widget.TagTextView
import com.lishuaihua.index.bean.SecondItemListBean
import org.json.JSONObject
import java.util.ArrayList

class GialenGoodsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var iv_goods_img: ImageView
    var iv_sellout: ImageView
    var tv_desc: TagTextView
    var ll_profit_or_share: LinearLayout
    var ll_profit: LinearLayout
    var tv_profit: TextView
    var tv_sale_price: TextView
    var tv_orign_price: TextView
    var tag_recycler_view: RecyclerView

    init {
        iv_goods_img = itemView.findViewById(R.id.iv_goods_img)
        iv_sellout = itemView.findViewById(R.id.iv_sellout)
        tv_desc = itemView.findViewById(R.id.tv_desc)
        ll_profit_or_share = itemView.findViewById(R.id.ll_profit_or_share)
        ll_profit = itemView.findViewById(R.id.ll_profit)
        tv_profit = itemView.findViewById(R.id.tv_profit)
        tv_sale_price = itemView.findViewById(R.id.tv_sale_price)
        tv_orign_price = itemView.findViewById(R.id.tv_orign_price)
        tag_recycler_view = itemView.findViewById(R.id.tag_recycler_view)

    }

    fun bindView(context: Context, productBean: SecondItemListBean, isVip:Boolean) {
        if (productBean != null) {

            val screenWidth = ScreenUtil.getScreenWidth(context)
            var imageViewWith = (screenWidth - CommonUtil.dp2px(context, 40f)) / 3
            val layoutParams = iv_goods_img.layoutParams
            layoutParams.width = imageViewWith
            layoutParams.height = imageViewWith
            com.lishuaihua.baselib.util.ImageLoader.setImageResource(context,
                    productBean.imgUrl,
                    R.mipmap.ic_default_logo, imageViewWith, imageViewWith, iv_goods_img)
            if (productBean.soldOut) {
                val layoutParams = iv_sellout.layoutParams
                layoutParams.width = imageViewWith
                layoutParams.height = imageViewWith
                iv_sellout.visibility = View.VISIBLE
            } else {
                iv_sellout.visibility = View.GONE
            }

            if (!StringUtils.isEmpty(productBean.takeMsg)) {
                tv_desc.setTextAndTag(productBean.title, productBean.takeMsg)
            } else {
                tv_desc.setText(productBean.title)
            }
            tv_sale_price.setText(String.format(context.getString(R.string.price), productBean.att.salePrice))
            tv_orign_price.setText(String.format(context.getString(R.string.price), productBean.att.marketPrice))
            tv_orign_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG)

            val listTagVOList = ArrayList<ListTagVO>()
            val tags = productBean.att.tags
            if (tags.size > 0) {
                for (tagBean in tags) {
                    var listTagVO = ListTagVO()
                    listTagVO.tagType = tagBean.tagType
                    listTagVO.tagValue = tagBean.tagValue
                    listTagVOList.add(listTagVO)
                }
                tag_recycler_view.visibility = View.VISIBLE
                TagUtils.setTag(context, tag_recycler_view, listTagVOList, 0)
            }
            tv_sale_price.setTextColor(Color.parseColor("#212121"))
            if (isVip){
                ll_profit_or_share.visibility = View.GONE
            }else{
                ll_profit_or_share.visibility = View.VISIBLE
                val profit = productBean.att.commission
                if (profit != null) {
                    ll_profit.visibility = View.VISIBLE
                    tv_profit.text=String.format(context.getString(R.string.price), productBean.att.commission)
                } else {
                    ll_profit.visibility = View.GONE
                }
            }

        }
    }
}
