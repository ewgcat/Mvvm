package com.lishuaihua.baselib.base

import android.app.Activity
import android.app.Application
import android.app.ProgressDialog
import android.content.ComponentCallbacks
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import java.lang.reflect.ParameterizedType
import androidx.lifecycle.ViewModelProvider
import com.lishuaihua.baselib.bus.LiveDataBus
import com.lishuaihua.baselib.sp.SharedPreferencesManager
import com.lishuaihua.baselib.widget.GrayFrameLayout
import com.lishuaihua.net.httputils.BaseViewModel


open abstract class BaseActivity<U : BaseViewModel>() : AppCompatActivity() {

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
        setCustomDensity(BaseApplication.instance, this)
        setContentView(resId)
        vm = createViewModel()
        doCreateView(savedInstanceState)
        LiveDataBus.get().with("UpdateGloableColor")!!.observe(this, {
            if ("UpdateGloableColor".equals(it)) {
                recreate()
            }
        })
        vm.isShowLoading.observe(this,  {
            if (it) showLoading() else dismissLoding()
        })
        vm.errorData.observe(this,  {
            Toast.makeText(this,it.msg, Toast.LENGTH_SHORT).show()
        })
    }

    private var aNoncompatDensity = 0f
    private var aNoncompatScaledDensity = 0f

    private fun setCustomDensity(application: Application, activity: Activity) {
        val appDisplayMetrics: DisplayMetrics = application.getResources().getDisplayMetrics()
        if (aNoncompatDensity == 0f) {
            aNoncompatDensity = appDisplayMetrics.density
            aNoncompatScaledDensity = appDisplayMetrics.scaledDensity
            application.registerComponentCallbacks(object : ComponentCallbacks {
                override fun onConfigurationChanged(@NonNull newConfig: Configuration) {
                    if (newConfig.fontScale > 0) {
                        aNoncompatScaledDensity =
                            application.getResources().getDisplayMetrics().scaledDensity
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
        val vmClass: Class<U> =
            (this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<U>
        return ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(vmClass)
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        var defaultGloableColor =
            SharedPreferencesManager.getInstance(context).getInt("defaultGloableColor", 0)
        if (defaultGloableColor == 1) {
            if (("FrameLayout" == name)) {
                var count: Int = attrs.getAttributeCount()
                for (i in 0 until count) {
                    var attributeName: String? = attrs.getAttributeName(i)
                    var attributeValue: String? = attrs.getAttributeValue(i)
                    if ((attributeName == "id")) {
                        var id: Int = attributeValue?.substring(1)!!.toInt()
                        var idVal: String? = getResources().getResourceName(id)
                        if (("android:id/content" == idVal)) {
                            var grayFrameLayout: GrayFrameLayout? = GrayFrameLayout(context, attrs)
                            return grayFrameLayout
                        }
                    }
                }
            }
        } else {
            if (("FrameLayout" == name)) {
                var count: Int = attrs.getAttributeCount()
                for (i in 0 until count) {
                    var attributeName: String? = attrs.getAttributeName(i)
                    var attributeValue: String? = attrs.getAttributeValue(i)
                    if ((attributeName == "id")) {
                        var id: Int = attributeValue?.substring(1)!!.toInt()
                        var idVal: String? = getResources().getResourceName(id)
                        if (("android:id/content" == idVal)) {
                            var frameLayout: FrameLayout? = FrameLayout(context, attrs)
                            return frameLayout
                        }
                    }
                }
            }
        }
        return super.onCreateView(name, context, attrs)
    }
    private var dialogLoading: ProgressDialog? = null
    fun showLoading() {
        if (dialogLoading == null) {
            dialogLoading = ProgressDialog(this)
        }
        dialogLoading!!.show()
    }

    fun dismissLoding() {
        dialogLoading?.dismiss()
        dialogLoading = null
    }
}