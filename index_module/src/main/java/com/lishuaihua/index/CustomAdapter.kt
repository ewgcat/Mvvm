package com.lishuaihua.index

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lishuaihua.baselib.bean.ListTagVO
import com.lishuaihua.baselib.util.CommonUtil
import com.lishuaihua.baselib.util.ScreenUtil
import com.lishuaihua.baselib.util.StringUtils
import com.lishuaihua.baselib.util.TagUtils
import com.lishuaihua.baselib.widget.TagTextView
import com.lishuaihua.imageloader.load
import com.lishuaihua.index.bean.ItemListItem
import com.lishuaihua.index.databinding.ViewItemBinding
import java.util.*

class CustomAdapter(var context: Context) : PagingDataAdapter<ItemListItem, CustomAdapter.CustomViewHolder>(CustomDiffUtilCallback()) {

    private val isVip=true

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        getItem(position)?.let {
          var  itemWith = (ScreenUtil.getScreenWidth(context) - CommonUtil.dp2px(context, 30f)) / 2
            val layoutParams1 =  holder.itemView.layoutParams
            layoutParams1.width = itemWith
            val layoutParams =  holder.iv_goods_img.layoutParams
            layoutParams.width = itemWith
            layoutParams.height = itemWith
            holder.iv_goods_img.load(it.imgUrl)

            if (it.soldOut) {
                val layoutParams =   holder.iv_sellout.layoutParams
                layoutParams.width = itemWith
                layoutParams.height = itemWith
                holder. iv_sellout.visibility = View.VISIBLE
            } else {
                holder. iv_sellout.visibility = View.GONE
            }
            if (!StringUtils.isEmpty(it.takeMsg) ) {
                holder. tv_desc.setTextAndTag(it.title, it.takeMsg!!)
            } else {
                holder. tv_desc.setText(it.title)
            }
            holder. tv_sale_price.setText(String.format(context.getString(R.string.price), it.att.salePrice))
            holder. tv_orign_price.setText(String.format(context.getString(R.string.price), it.att.marketPrice))
            holder. tv_orign_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG)
            val listTagVOList = ArrayList<ListTagVO>()
            val tags = it.att.tags
            if (tags.size > 0) {
                for (tagBean in tags) {
                    var listTagVO = ListTagVO()
                    listTagVO.tagType = tagBean.tagType
                    listTagVO.tagValue = tagBean.tagValue
                    listTagVOList.add(listTagVO)
                }
                holder. tag_recycler_view.visibility = View.VISIBLE
                TagUtils.setTag(context,   holder.tag_recycler_view, listTagVOList, 0)
            }
            holder.tv_share_goods.setOnClickListener {

            }
            holder. tv_sale_price.setTextColor(Color.parseColor("#212121"))
            if (isVip){
                holder.ll_profit_or_share.visibility = View.GONE
                holder.tv_share_goods.visibility = View.GONE
            }else{
                holder.ll_profit_or_share.visibility = View.VISIBLE
                holder.tv_share_goods.visibility = View.VISIBLE
                val profit = it.att.commission
                if (profit != null) {
                    holder. ll_profit.visibility = View.VISIBLE
                    holder. tv_profit.text=String.format(context.getString(R.string.price), it.att.commission)
                } else {
                    holder.ll_profit.visibility = View.GONE
                    holder. tv_share_goods.visibility = View.GONE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_item,parent))
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iv_goods_img = itemView.findViewById<ImageView>(R.id.iv_goods_img)
        val iv_sellout = itemView.findViewById<ImageView>(R.id.iv_sellout)
        val tv_desc = itemView.findViewById<TagTextView>(R.id.tv_desc)
        val tv_sale_price = itemView.findViewById<TextView>(R.id.tv_sale_price)
        val tv_orign_price = itemView.findViewById<TextView>(R.id.tv_orign_price)
        val tv_share_goods = itemView.findViewById<TextView>(R.id.tv_share_goods)
        val tv_profit = itemView.findViewById<TextView>(R.id.tv_profit)
        val ll_profit_or_share = itemView.findViewById<LinearLayout>(R.id.ll_profit_or_share)
        val ll_profit = itemView.findViewById<LinearLayout>(R.id.ll_profit)
        val tag_recycler_view = itemView.findViewById<RecyclerView>(R.id.tag_recycler_view)
    }

    class CustomDiffUtilCallback : DiffUtil.ItemCallback<ItemListItem>() {
        override fun areItemsTheSame(oldItem: ItemListItem, newItem: ItemListItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ItemListItem, newItem: ItemListItem): Boolean {
            return oldItem.id == newItem.id
        }
    }

}