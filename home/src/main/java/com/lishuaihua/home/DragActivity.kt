package com.lishuaihua.home

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.lishuaihua.baselib.base.BaseActivity
import com.lishuaihua.baselib.binding.ext.viewbind
import com.lishuaihua.home.databinding.ActivityDragBinding

@Route(path = "/home/drag")
class DragActivity: BaseActivity() {
    private val binding: ActivityDragBinding by viewbind()
    override fun getLayoutResId(): Int =R.layout.activity_drag

    override fun doCreateView(savedInstanceState: Bundle?) {
        binding.dragView.addDragView(R.layout.my_self_view, 0, 400, 380, 760, true, false)

        binding.button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                binding.dragView.addDragView(R.layout.my_self_view, 0, 400, 380, 760, true, false);
            }
        })

        binding.button2.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                binding.dragView.addDragView(R.layout.my_self_view, 0, 400, 380, 760, false, false)
            }
        })

        binding.button3.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                val tt = TextView(context)
                tt.text = "今天大雨，出门记得带伞哦！"
                tt.setTextColor(Color.BLACK)
                binding.dragView.addDragView(tt, 50, 50, 700, 100, true, true)
            }
        })
    }

}