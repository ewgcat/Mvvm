package com.lishuaihua.baselib.base

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.lishuaihua.baselib.ext.addObserver
import com.lishuaihua.baselib.ext.bindMethod
import com.lishuaihua.baselib.ext.inflateMethod
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType


open abstract class BaseFragment< U : ViewModel>() : Fragment() {

    protected lateinit var vm: U

    
    protected var mActivity: Activity? = null
    protected var rootView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layoutResId = getLayoutId()
        require(layoutResId > 0) { "The subclass must provider a valid layout resources id." }
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), null)
        }
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), null)
        } else {
            val parent = rootView?.parent as ViewGroup?
            parent?.removeView(rootView)
        }
        requireActivity().window.decorView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                requireActivity().window.decorView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                lazzyLoad()
            }
        })
        mActivity = activity
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doCreateView(savedInstanceState)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = createViewModel()
    }
    /**
     * 加载布局
     */
    abstract fun getLayoutId(): Int
    protected open fun lazzyLoad() {

    }


    /**
     * Initialize view model according to the generic class type. Override this method to
     * add your owen implementation.
     * @return the view model instance.
     */
    protected fun createViewModel(): U {
        val vmClass: Class<U> = (this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<U>
        return ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(vmClass)
    }
    
    protected abstract fun doCreateView(savedInstanceState: Bundle?)
    override fun onDestroyView() {
        super.onDestroyView()
        rootView = null
    }


}
