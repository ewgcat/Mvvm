package com.lishuaihua.baselib.binding

import android.app.Activity
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.lishuaihua.baselib.binding.ActivityDataBindingDelegate


inline fun <reified T : ViewBinding> AppCompatActivity.binding() =
    ActivityDataBindingDelegate(T::class.java, this)

inline fun <reified T : ViewBinding> FragmentActivity.binding() =
    ActivityDataBindingDelegate(T::class.java, this)

inline fun <reified T : ViewBinding> Activity.binding() =
    ActivityDataBindingDelegate(T::class.java, this)



