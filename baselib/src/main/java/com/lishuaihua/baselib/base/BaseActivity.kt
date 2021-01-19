package com.lishuaihua.baselib.base

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
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
        setCustomDensity(BaseApplication.instance,this)
        setContentView(resId)
        vm = createViewModel()
        doCreateView(savedInstanceState)
    }
    private var aNoncompatDensity = 0f
    private var aNoncompatScaledDensity = 0f

    private  fun setCustomDensity(application: Application, activity: Activity) {
        val appDisplayMetrics: DisplayMetrics = application.getResources().getDisplayMetrics()
        if (aNoncompatDensity == 0f) {
            aNoncompatDensity = appDisplayMetrics.density
            aNoncompatScaledDensity = appDisplayMetrics.scaledDensity
            application.registerComponentCallbacks(object : ComponentCallbacks {
                override fun onConfigurationChanged(@NonNull newConfig: Configuration) {
                    if (newConfig.fontScale > 0) {
                        aNoncompatScaledDensity = application.getResources().getDisplayMetrics().scaledDensity
                    }
                }

                override fun onLowMemory() {}
            })
        }
        val targetDensity = (appDisplayMetrics.widthPixels / 360).toFloat()
        val targetScaledDensity = targetDensity * (aNoncompatScaledDensity / aNoncompatDensity)
        val targetDensityDpi = (targetDensity * 160).toInt()

        //设置application的Density
        appDisplayMetrics.density = targetDensity
        appDisplayMetrics.scaledDensity = targetScaledDensity
        appDisplayMetrics.densityDpi = targetDensityDpi

        //设置activity的Density
        val activityDisplayMetrics = activity.resources.displayMetrics
        activityDisplayMetrics.density = targetDensity
        activityDisplayMetrics.scaledDensity = targetScaledDensity
        activityDisplayMetrics.densityDpi = targetDensityDpi
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