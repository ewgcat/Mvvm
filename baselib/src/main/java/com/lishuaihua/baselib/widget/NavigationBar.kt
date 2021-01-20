package com.lishuaihua.baselib.widget

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.lishuaihua.baselib.R
import com.lishuaihua.baselib.databinding.ViewNavigationBarBinding

class NavigationBar :LinearLayout {
    private var firstLeftIv: ImageView? = null
    private var mTitleView: TextView? = null
    private var mRightTv: TextView? = null
    var backLL: LinearLayout? = null
        private set
    private var bottomLine: View? = null
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!,
        attrs,
        defStyleAttr
    ) {
      val binding=  DataBindingUtil.inflate<ViewNavigationBarBinding>(
            LayoutInflater.from(context),
            R.layout.view_navigation_bar,
            this,
            true
        )
        backLL=binding.llBack
        firstLeftIv=binding.ivFirstLeft
        mTitleView=binding.titleTv
        mRightTv=binding.rightTv
        bottomLine=binding.bottomLine
    }


    /*
     * @see android.view.View#getBottomFadingEdgeStrength()
     */
    override fun getBottomFadingEdgeStrength(): Float {
        return 1.0f
    }



    fun getmTitleView(): TextView? {
        return mTitleView
    }

    fun getmRightTv(): TextView? {
        return mRightTv
    }

    fun setTitle(title: String) {
        mTitleView?.text = title
    }

    fun setTitleColor(color: Int) {
        mTitleView?.setTextColor(color)
    }

    fun setmRightTvText(text: String) {
        mRightTv?.text = text
    }

    fun setmRightTvColor(color: Int) {
        mRightTv?.setTextColor(color)
    }

    fun setBackLLVisiable(i: Int) {
        backLL?.visibility = i
    }

    fun hideBottomLine() {
        bottomLine?.visibility = View.GONE
    }



    fun setBackClickListener(activity: Activity) {
        backLL?.setOnClickListener { activity.finish() }
    }

    fun setBackOnClickListener(listener: View.OnClickListener) {
        backLL?.setOnClickListener(listener)
    }


    fun setRightClickListener(listener: View.OnClickListener) {
        mRightTv?.setOnClickListener(listener)
    }

    fun setRightClickAreaVisiable(v: Int) {
        mRightTv?.visibility = v
    }



}