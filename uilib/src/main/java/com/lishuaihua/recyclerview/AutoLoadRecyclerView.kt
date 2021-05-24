package com.lishuaihua.recyclerview

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AutoLoadRecyclerView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context!!, attrs, defStyle) {
    private fun init() {
        addOnScrollListener(ImageAutoLoadScrollListener())
    }

    inner class ImageAutoLoadScrollListener :
        OnScrollListener() {
        override fun onScrolled(
            recyclerView: RecyclerView,
            dx: Int,
            dy: Int
        ) {
            super.onScrolled(recyclerView, dx, dy)
        }

        override fun onScrollStateChanged(
            recyclerView: RecyclerView,
            newState: Int
        ) {
            super.onScrollStateChanged(recyclerView, newState)
            when (newState) {
                SCROLL_STATE_IDLE ->
                    try {
                        if (context != null) Glide.with(context).resumeRequests()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                SCROLL_STATE_DRAGGING ->
                    try {
                        if (context != null) Glide.with(context).pauseRequests()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                SCROLL_STATE_SETTLING ->
                    try {
                        if (context != null) Glide.with(context).pauseRequests()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
            }
        }
    }

    init {
        init()
    }
}