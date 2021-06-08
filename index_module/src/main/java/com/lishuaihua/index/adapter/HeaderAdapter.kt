package com.lishuaihua.index.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.lishuaihua.index.adv.AdvViewHolder
import com.lishuaihua.index.banner.BannerViewHolder
import com.lishuaihua.index.gialen_optimization.OptimizationViewHolder
import com.lishuaihua.index.hot_sales.HotSaleViewHolder
import com.lishuaihua.index.hotarea.Hot_AreaViewHolder
import com.lishuaihua.index.limittime.TimeLimitViewHolder
import com.lishuaihua.index.magic_cube.CubeViewHolder
import com.lishuaihua.index.navigation.NavigationViewHolder
import com.lishuaihua.index.product_floor.ProductFloorViewHolder
import com.lishuaihua.banner.Banner
import com.lishuaihua.baselib.util.CommonUtil
import com.lishuaihua.baselib.util.ScreenUtil
import com.lishuaihua.index.R
import com.lishuaihua.index.bean.IndexData
import java.util.ArrayList

class HeaderAdapter (private val context: Context, private val onPageChangeListener: ViewPager.OnPageChangeListener?, val isVip:Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var indexDataList: ArrayList<IndexData>
    private var inflater: LayoutInflater

    init {
        indexDataList = ArrayList<IndexData>()
        inflater = LayoutInflater.from(context)
    }

    fun setIndexDataList(indexDataList: ArrayList<IndexData>) {
        this.indexDataList.clear()
        this.indexDataList.addAll(indexDataList)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if ("hotArea".equals(indexDataList[position].type)) {
            0
        } else if ("indexBanner".equals(indexDataList[position].type)) {
            1
        } else if ("categoryNavigation".equals(indexDataList[position].type)) {
            2
        } else if ("indexAdv".equals(indexDataList[position].type)) {
            3
        } else if ("magicCube".equals(indexDataList[position].type)) {
            4
        } else if ("limittimeSales".equals(indexDataList[position].type)) {
            5
        } else if ("hotSales".equals(indexDataList[position].type)) {
            6
        } else if  ("xiaojiaoOptimization".equals(indexDataList[position].type)) {
            7
        }else if  ("productFloor".equals(indexDataList[position].type)) {
            9
        } else {
            8
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0) {
            val view = inflate(parent, R.layout.view_item_hotarea)
            return Hot_AreaViewHolder(view)
        } else  if (viewType == 1) {
            val view = inflate(parent, R.layout.view_item_banner)
            val banner = view.findViewById<Banner>(R.id.banner)
            val layoutParams = banner.layoutParams
            val width = ScreenUtil.getScreenWidth(context) - CommonUtil.dp2px(context, 20f)
            layoutParams.width = width
            layoutParams.height = width / 2
            banner.layoutParams = layoutParams
            return BannerViewHolder(view)
        } else if (viewType == 2) {
            val view = inflate(parent, R.layout.view_item_navigation)
            return NavigationViewHolder(view)
        } else if (viewType == 3) {
            val view = inflate(parent, R.layout.view_item_index_adv)
            return AdvViewHolder(view)
        }else if (viewType == 4) {
            val view = inflate(parent, R.layout.view_item_magic_cube)
            return CubeViewHolder(view)
        }else if (viewType == 5) {
            val view = inflate(parent, R.layout.view_item_limittime_sales)
            return TimeLimitViewHolder(view)
        } else if (viewType == 6){
            val view = inflate(parent, R.layout.view_item_hot_sale)
            return HotSaleViewHolder(view)
        }else if (viewType == 7){
            val view = inflate(parent, R.layout.view_item_gialen_optimization)
            return OptimizationViewHolder(view)
        }else {
            val view = inflate(parent, R.layout.view_item_product_floor)
            return ProductFloorViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == 0) {
            val bannerViewHolder = holder as Hot_AreaViewHolder
            bannerViewHolder.bindView(context,indexDataList!![position]!!)
        } else  if (getItemViewType(position) == 1) {
            val bannerViewHolder = holder as BannerViewHolder
            bannerViewHolder.bindView(indexDataList!![position],onPageChangeListener!!)
        } else if (getItemViewType(position) == 2){
            val navigationViewHolder = holder as NavigationViewHolder
            navigationViewHolder.bindView(context,indexDataList!![position])
        } else if (getItemViewType(position) == 3){
            val advViewHolder = holder as AdvViewHolder
            advViewHolder.bindView(context,indexDataList!![position])
        }else if (getItemViewType(position) == 4){
            val cubeViewHolder = holder as CubeViewHolder
            cubeViewHolder.bindView(context,indexDataList!![position])
        }else if (getItemViewType(position) == 5){
            val timeLimitViewHolder = holder as TimeLimitViewHolder
            timeLimitViewHolder.bindView(context,indexDataList!![position],isVip)
        }else if (getItemViewType(position) == 6){
            val hotSaleViewHolder = holder as HotSaleViewHolder
            hotSaleViewHolder.bindView(context,indexDataList!![position])
        }else if (getItemViewType(position) == 7){
            val optimizationViewHolder = holder as OptimizationViewHolder
            optimizationViewHolder.bindView(context,indexDataList!![position],isVip)
        }else if (getItemViewType(position) == 9){
            val productFloorViewHolder = holder as ProductFloorViewHolder
            productFloorViewHolder.bindView(context,indexDataList!![position],isVip)
        }
    }

    override fun getItemCount(): Int {
        return indexDataList!!.size
    }

    private fun inflate(parent: ViewGroup, layoutRes: Int): View {
        return inflater.inflate(layoutRes, parent, false)
    }




}
