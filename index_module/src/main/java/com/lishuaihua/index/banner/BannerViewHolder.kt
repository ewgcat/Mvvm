package com.lishuaihua.index.banner

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager

import com.lishuaihua.index.R
import com.lishuaihua.baselib.util.GlideRoundImageLoader
import com.lishuaihua.banner.Banner
import com.lishuaihua.banner.BannerConfig
import com.lishuaihua.banner.Transformer
import com.lishuaihua.index.bean.IndexData
import org.json.JSONObject

import java.util.ArrayList

class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var banner: Banner

    init {
        banner = itemView.findViewById(R.id.banner)
    }

    fun bindView(indexData: IndexData, onPageChangeListener: ViewPager.OnPageChangeListener) {
        val itemList = indexData.itemList
        val images = ArrayList<String>()
        for (i in itemList.indices) {
            images.add(itemList[i].imgUrl)
        }
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
        //设置图片加载器
        banner.setImageLoader(GlideRoundImageLoader())
        //设置图片集合
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage)
        //设置自动轮播，默认为true
        banner.isAutoPlay(true)
        //设置轮播时间
        banner.setDelayTime(5000)
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.RIGHT)
        banner.setImages(images)
        banner.setOnPageChangeListener(onPageChangeListener)
        banner.start()

    }
}
