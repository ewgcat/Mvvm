package com.lishuaihua.index.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lishuaihua.baselib.base.BaseApplication
import com.lishuaihua.baselib.bean.ListTagVO
import com.lishuaihua.baselib.util.CommonUtil
import com.lishuaihua.baselib.util.ScreenUtil
import com.lishuaihua.baselib.util.StringUtils
import com.lishuaihua.baselib.util.TagUtils
import com.lishuaihua.baselib.widget.TagTextView
import com.lishuaihua.index.R
import com.lishuaihua.index.bean.ItemListItem
import com.lishuaihua.paging3.adapter.ItemHelper
import com.lishuaihua.paging3.simple.SimpleHolder
import java.util.ArrayList

class IndexHolder() : SimpleHolder<ItemListItem>(R.layout.view_item) {
    var isVip=true
    var context:Context=BaseApplication.instance

    override fun bindItem(item: ItemHelper, data: ItemListItem, payloads: MutableList<Any>?) {
        var itemWith = (ScreenUtil.getScreenWidth(context) - CommonUtil.dp2px(context!!, 30f))/2
        var item_view = containerView!!.findViewById<CardView>(R.id.item_view)
        val layoutParams = item_view.layoutParams
        layoutParams.width = itemWith

        val ivGoodsImg = containerView!!.findViewById<ImageView>(R.id.iv_goods_img)
        val goodsLayoutParams = ivGoodsImg.layoutParams
        goodsLayoutParams.width = itemWith
        goodsLayoutParams.height = itemWith
        val ivSellOut = containerView!!.findViewById<ImageView>(R.id.iv_sellout)
        if (data!!.soldOut) {
            val layoutParams = ivSellOut.layoutParams
            layoutParams.width = itemWith
            layoutParams.height = itemWith
            ivSellOut.visibility = View.VISIBLE
        } else {
            ivSellOut.visibility = View.GONE
        }
//        Log.d("IndexHolder","imgUrl="+data.imgUrl)
//        ivGoodsImg.load(data.imgUrl)
            Glide.with(ivGoodsImg.context).load(data.imgUrl).into(ivGoodsImg)
        var tvDesc = containerView!!.findViewById<TagTextView>(R.id.tv_desc)
        if (!StringUtils.isEmpty(data.takeMsg)) {
            tvDesc.setTextAndTag(data.title, data.takeMsg!!)
        } else {
            tvDesc.setText(data.title)
        }
        var tvSalePrice = containerView!!.findViewById<TextView>(R.id.tv_sale_price)
        var tvOrignPrice = containerView!!.findViewById<TextView>(R.id.tv_orign_price)
        tvSalePrice.setText(String.format(context!!.getString(R.string.price), data.att.salePrice))
        tvOrignPrice.setText(String.format(context!!.getString(R.string.price), data.att.marketPrice))
        tvOrignPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG)
        val listTagVOList = ArrayList<ListTagVO>()
        val tags = data.att.tags
        if (tags.size > 0) {
            for (tagBean in tags) {
                var listTagVO = ListTagVO()
                listTagVO.tagType = tagBean.tagType
                listTagVO.tagValue = tagBean.tagValue
                listTagVOList.add(listTagVO)
            }
            var tagRecyclerView = containerView!!.findViewById<RecyclerView>(R.id.tag_recycler_view)
            tagRecyclerView.visibility = View.VISIBLE
            TagUtils.setTag(context, tagRecyclerView, listTagVOList, 0)
        }
        tvSalePrice.setTextColor(Color.parseColor("#212121"))
        val llProfitOrShare = containerView!!.findViewById<LinearLayout>(R.id.ll_profit_or_share)
        val llProfit = containerView!!.findViewById<LinearLayout>(R.id.ll_profit)
        val tvShareGoods = containerView!!.findViewById<TextView>(R.id.tv_share_goods)
        val tvProfit = containerView!!.findViewById<TextView>(R.id.tv_profit)
        if (isVip) {
            llProfitOrShare.visibility = View.GONE
            tvShareGoods.visibility = View.GONE
        } else {
            llProfitOrShare.visibility = View.VISIBLE
            tvShareGoods.visibility = View.VISIBLE
            val profit = data.att.commission
            if (profit != null) {
                llProfit.visibility = View.VISIBLE
                tvProfit.text =
                    String.format(context!!.getString(R.string.price), data.att.commission)
            } else {
                llProfit.visibility = View.GONE
                tvShareGoods.visibility = View.GONE
            }
        }
    }


}