package com.lishuaihua.baselib.binding

import android.app.Dialog
import androidx.lifecycle.Lifecycle
import androidx.viewbinding.ViewBinding
import com.lishuaihua.baselib.ext.inflateMethod
import kotlin.reflect.KProperty


class DialogViewBinding<T : ViewBinding>(
    classes: Class<T>,
    lifecycle: Lifecycle? = null
) : DialogDelegate<T>(lifecycle) {

    private var layoutInflater = classes.inflateMethod()

    override fun getValue(thisRef: Dialog, property: KProperty<*>): T {
        return viewBinding?.run {
            this

        } ?: let {

            val bind = layoutInflater.invoke(null, thisRef.layoutInflater) as T
            thisRef.setContentView(bind.root)
            return bind.apply { viewBinding = this }
        }

    }

}