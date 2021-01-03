package com.lishuaihua.baselib.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import java.lang.reflect.ParameterizedType
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.lishuaihua.baselib.ext.addObserver


open abstract class BaseActivity< U : BaseViewModel>() : AppCompatActivity() {

    @LayoutRes
    var resId: Int = 0

    protected lateinit var vm: U
        private set



    @LayoutRes
    protected abstract fun getLayoutResId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resId = getLayoutResId()
        require(resId > 0) { "The subclass must provider a valid layout resources id." }
        setContentView(resId)
        vm = createViewModel()
        doCreateView(savedInstanceState)
    }

    protected abstract fun doCreateView(savedInstanceState: Bundle?)

    /**
     * Initialize view model. Override this method to add your own implementation.
     *
     * @return the view model will be used.
     */
    protected fun createViewModel(): U {
        val vmClass: Class<U> = (this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<U>
        return ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(vmClass)
    }
}