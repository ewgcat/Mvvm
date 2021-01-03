package com.lishuaihua.baselib.binding

import android.app.Activity
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding
import com.lishuaihua.baselib.ext.LifecycleFragment
import kotlin.properties.ReadOnlyProperty
import com.lishuaihua.baselib.ext.addObserver
import com.lishuaihua.baselib.ext.registerActivityLifecycleCallbacks
abstract class ActivityDelegate<T : ViewBinding>(
    activity: Activity
) : ReadOnlyProperty<Activity, T> {

    protected var viewBinding: T? = null
    private val LIFECYCLE_FRAGMENT_TAG = "com.lishuaihua.binding.lifecycle_fragment"

    init {
        when (activity) {
            is ComponentActivity -> activity.lifecycle.addObserver { destroyed() }
            else -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    activity.registerActivityLifecycleCallbacks { destroyed() }
                }
            }
        }

    }

    /**
     * 当继承 Activity 且 Build.VERSION.SDK_INT < Build.VERSION_CODES.Q 以下的时候，
     * 会添加一个 空白的 Fragment, 当生命周期处于 onDestroy 时销毁数据
     */
    fun addLifecycleFragment(activity: Activity) {
        if (activity is FragmentActivity || activity is AppCompatActivity) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) return

        val fragmentManager = activity.fragmentManager
        if (fragmentManager.findFragmentByTag(LIFECYCLE_FRAGMENT_TAG) == null) {
            val transaction = fragmentManager.beginTransaction()
            transaction.add(LifecycleFragment { destroyed() }, LIFECYCLE_FRAGMENT_TAG).commit()
            fragmentManager.executePendingTransactions()
        }
    }

    private fun destroyed() {
        viewBinding = null
    }
}