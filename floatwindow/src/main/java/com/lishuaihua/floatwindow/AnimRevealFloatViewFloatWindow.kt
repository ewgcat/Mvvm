package com.lishuaihua.floatwindow

import android.widget.LinearLayout
import android.widget.Toast
import android.view.Gravity
import android.widget.RelativeLayout
import android.view.ViewGroup
import com.blankj.utilcode.util.SizeUtils
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import java.lang.Exception

class AnimRevealFloatViewFloatWindow(context: Context?) : BaseFloatWindow(
    context!!
) {
    private val lLRoot: LinearLayout?
    override fun onAddWindowFailed(e: Exception?) {
        Toast.makeText(mContext, e!!.localizedMessage, Toast.LENGTH_SHORT).show()
    }

    init {
        mViewMode = WRAP_CONTENT_TOUCHABLE
        mGravity = Gravity.END or Gravity.CENTER_VERTICAL
        inflate(R.layout.main_layout_anim_reveal_window)
        val rLRoot = findView<RelativeLayout>(R.id.rlRoot)
        lLRoot = findView<LinearLayout>(R.id.lLRoot)
        rLRoot!!.post {
            val layoutParams = rLRoot.layoutParams
            layoutParams.width = SizeUtils.dp2px(120f)
            rLRoot.layoutParams = layoutParams
            lLRoot!!.postDelayed({
                val layoutParams = lLRoot.layoutParams
                val valueAnimator = ValueAnimator.ofInt(SizeUtils.dp2px(36f), SizeUtils.dp2px(120f))
                valueAnimator.addUpdateListener { valueAnimator ->
                    layoutParams.width = valueAnimator.animatedValue as Int
                    lLRoot.layoutParams = layoutParams
                }
                valueAnimator.duration = 1000
                valueAnimator.start()
            }, 10)
        }
    }
}