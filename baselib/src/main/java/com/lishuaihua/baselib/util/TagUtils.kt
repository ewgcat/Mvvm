package com.lishuaihua.baselib.util

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lishuaihua.baselib.adapter.ListTapAdapter
import com.lishuaihua.baselib.bean.ListTagVO
import java.util.*

/**
 * 标签工具栏
 */
object TagUtils {
    fun setTag(context: Context?, recyclerView: RecyclerView, list: List<ListTagVO>, type: Int) {
        if (list != null) {
            if (list.size > 0) {
                recyclerView.visibility = View.VISIBLE
                var layoutManager: LinearLayoutManager? = null
                layoutManager = LinearLayoutManager(context)
                layoutManager.orientation = LinearLayoutManager.HORIZONTAL
                recyclerView.layoutManager = layoutManager
                val mAdapter = ListTapAdapter(list, type)
                recyclerView.adapter = mAdapter
            } else {
                recyclerView.visibility = View.GONE
            }
        } else {
            recyclerView.visibility = View.GONE
        }
    }
}