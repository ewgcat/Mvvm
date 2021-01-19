package com.lishuaihua.baselib.binding

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.lishuaihua.baselib.ext.bindMethod
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


inline fun <reified T : ViewBinding> Fragment.binding() =
    FragmentBindingDelegate(T::class.java, this)

class FragmentBindingDelegate<T : ViewBinding>(
    classes: Class<T>,
    fragment: Fragment
) : ReadOnlyProperty<Fragment, T> {

    var viewBinding: T? = null
    val bindView = classes.bindMethod()


    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return viewBinding?.run {
            return this
        } ?: let {
            val bind = bindView.invoke(null, thisRef.view) as T
            return bind.apply { viewBinding = this }
        }
    }


}