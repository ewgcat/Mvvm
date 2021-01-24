package com.lishuaihua.baselib.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.lishuaihua.baselib.R
import com.lishuaihua.baselib.util.StringUtils
import java.util.*

class TagTextView : AppCompatTextView {
    private var content_buffer: StringBuffer? = null
    private var tv_tag: TextView? = null
    private val view //标签布局的最外层布局
            : View? = null
    private var mContext: Context

    //必须重写所有的构造器，否则可能会出现无法inflate布局的错误！
    constructor(context: Context) : super(context) {
        mContext = context
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mContext = context
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mContext = context
    }

    fun setTextAndTag(content: String?, takeMsg: String) {
        val tags: MutableList<String> = ArrayList()
        tags.add(takeMsg)
        content_buffer = StringBuffer()
        for (item in tags) { //将每个tag的内容添加到content后边，之后将用drawable替代这些tag所占的位置
            content_buffer!!.append(item)
        }
        content_buffer!!.append(content)
        val spannableString = SpannableString(content_buffer)
        for (i in tags.indices) {
            val item = tags[i]
            val view = LayoutInflater.from(mContext).inflate(R.layout.view_tag, null) //R.layout.tag是每个标签的布局
            tv_tag = view.findViewById(R.id.tv_tag)
            tv_tag!!.setText(item)
            val bitmap = convertViewToBitmap(view)
            val d: Drawable = BitmapDrawable(bitmap)
            d.setBounds(0, 0, tv_tag!!.getWidth(), tv_tag!!.getHeight()) //缺少这句的话，不会报错，但是图片不回显示
            val span = ImageSpan(d, ImageSpan.ALIGN_BOTTOM) //图片将对齐底部边线
            var startIndex: Int
            var endIndex: Int
            startIndex = getLastLength(tags, i)
            endIndex = startIndex + item.length
            Log.e("tag", "the start is" + startIndex + "the end is" + endIndex)
            spannableString.setSpan(span, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        text = spannableString
        gravity = Gravity.CENTER_VERTICAL
    }

    fun setTagText(tag: String) {
        val tags: MutableList<String> = ArrayList()
        if (!StringUtils.isEmpty(tag)) {
            tags.add(tag)
        }
        content_buffer = StringBuffer()
        for (item in tags) { //将每个tag的内容添加到content后边，之后将用drawable替代这些tag所占的位置
            content_buffer!!.append(item)
        }
        val spannableString = SpannableString(content_buffer)
        for (i in tags.indices) {
            val item = tags[i]
            val view = LayoutInflater.from(mContext).inflate(R.layout.view_tag, null) //R.layout.tag是每个标签的布局
            tv_tag = view.findViewById(R.id.tv_tag)
            tv_tag!!.setText(item)
            val bitmap = convertViewToBitmap(view)
            val d: Drawable = BitmapDrawable(bitmap)
            d.setBounds(0, 0, tv_tag!!.getWidth(), tv_tag!!.getHeight()) //缺少这句的话，不会报错，但是图片不回显示
            val span = ImageSpan(d, ImageSpan.ALIGN_BOTTOM) //图片将对齐底部边线
            var startIndex: Int
            var endIndex: Int
            startIndex = getLastLength(tags, i)
            endIndex = startIndex + item.length
            Log.e("tag", "the start is" + startIndex + "the end is" + endIndex)
            spannableString.setSpan(span, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        text = spannableString
        gravity = Gravity.CENTER_VERTICAL
    }

    private fun getLastLength(list: List<String>, maxLength: Int): Int {
        var length = 0
        for (i in 0 until maxLength) {
            length += list[i].length
        }
        return length
    }

    companion object {
        private fun convertViewToBitmap(view: View): Bitmap {
            view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
            view.layout(0, 0, view.measuredWidth, view.measuredHeight)
            view.buildDrawingCache()
            return view.drawingCache
        }
    }
}