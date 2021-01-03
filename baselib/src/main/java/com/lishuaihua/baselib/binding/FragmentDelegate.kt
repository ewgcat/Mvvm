package com.lishuaihua.baselib.binding


import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import com.lishuaihua.baselib.ext.addObserver

abstract class FragmentDelegate<T : ViewBinding>(
    fragment: Fragment
) : ReadOnlyProperty<Fragment, T> {

    protected var viewBinding: T? = null

    init {
        fragment.lifecycle.addObserver { destroyed() }
    }

    private fun destroyed() {
        viewBinding = null
    }
}