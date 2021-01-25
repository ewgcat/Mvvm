package com.lishuaihua.index

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.lishuaihua.baselib.bean.ListTagVO
import com.lishuaihua.baselib.util.CommonUtil
import com.lishuaihua.baselib.util.ScreenUtil
import com.lishuaihua.baselib.util.StringUtils
import com.lishuaihua.baselib.util.TagUtils
import com.lishuaihua.baselib.widget.TagTextView
import com.lishuaihua.imageloader.load
import com.lishuaihua.index.bean.ItemListItem
import com.lishuaihua.paging3.PagingDataAdapterKtx
import com.lishuaihua.paging3.SimpleHolder
import java.util.ArrayList

class IndexHolder(val context: Context,val isVip:Boolean) : SimpleHolder<ItemListItem>() {

    override fun getLayoutRes(): Int {
        return R.layout.view_item
    }

    override fun bindData(
        helper: PagingDataAdapterKtx.ItemHelper,
        data: ItemListItem?,
        payloads: MutableList<Any>?
    ) {
        if (data == null) return
        var  itemWith = (ScreenUtil.getScreenWidth(context) - CommonUtil.dp2px(context, 30f)) / 2
        var itemView=helper.findViewById<CardView>(R.id.item_view)
        val ivGoodsImg = helper.findViewById<ImageView>(R.id.iv_goods_img)
        val layoutParams =  itemView.layoutParams
        layoutParams.width = itemWith
        val goodsLayoutParams =  ivGoodsImg.layoutParams
        goodsLayoutParams.width = itemWith
        goodsLayoutParams.height = itemWith
        ivGoodsImg.load(data.imgUrl)
        val ivSellOut = helper.findViewById<ImageView>(R.id.iv_sellout)
        if (data.soldOut) {
            val layoutParams = ivSellOut.layoutParams
            layoutParams.width = itemWith
            layoutParams.height = itemWith
            ivSellOut.visibility = View.VISIBLE
        } else {
            ivSellOut.visibility = View.GONE
        }
        if (!StringUtils.isEmpty(data.takeMsg)) {
            var tvDesc=helper.findViewById<TagTextView>(R.id.tv_desc)
            tvDesc.setTextAndTag(data.title, data.takeMsg!!)
        } else {
            helper.setText(R.id.tv_desc, data.title)
        }
        helper.setText(R.id.tv_sale_price,  String.format(
            context.getString(R.string.price),
            data.att.salePrice
        ))
       helper.setText(R.id.tv_orign_price,  String.format(
            context.getString(R.string.price),
            data.att.marketPrice
        ))

        var tvOrignPrice=helper.findViewById<TextView>(R.id.tv_orign_price)
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
            var tagRecyclerView=helper.findViewById<RecyclerView>(R.id.tag_recycler_view)
            tagRecyclerView.visibility = View.VISIBLE
            TagUtils.setTag(context, tagRecyclerView, listTagVOList, 0)
        }
        helper.setTextColor(R.id.tv_sale_price, Color.parseColor("#212121"))
        val llProfitOrShare = helper.findViewById<LinearLayout>(R.id.ll_profit_or_share)
        val llProfit = helper.findViewById<LinearLayout>(R.id.ll_profit)
        val tvShareGoods = helper.findViewById<TextView>(R.id.tv_share_goods)
        val tvProfit = helper.findViewById<TextView>(R.id.tv_profit)
        if (isVip) {
            llProfitOrShare.visibility = View.GONE
            tvShareGoods.visibility = View.GONE
        } else {
            llProfitOrShare.visibility = View.VISIBLE
            tvShareGoods.visibility = View.VISIBLE
            val profit = data.att.commission
            if (profit != null) {
                llProfit.visibility = View.VISIBLE
                tvProfit.text = String.format(context.getString(R.string.price), data.att.commission)
            } else {
                llProfit.visibility  = View.GONE
                tvShareGoods.visibility = View.GONE
            }
        }
    }
}