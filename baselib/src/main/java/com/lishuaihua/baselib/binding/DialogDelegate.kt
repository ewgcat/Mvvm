package com.lishuaihua.baselib.binding

import android.app.Dialog
import androidx.lifecycle.Lifecycle
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import com.lishuaihua.baselib.ext.addObserver

abstract class DialogDelegate<T : ViewBinding>(
    lifecycle: Lifecycle? = null
) : ReadOnlyProperty<Dialog, T> {

    protected var viewBinding: T? = null

    init {
        lifecycle?.addObserver { destroyed() }
    }

    fun destroyed() {
        viewBinding = null
    }
}