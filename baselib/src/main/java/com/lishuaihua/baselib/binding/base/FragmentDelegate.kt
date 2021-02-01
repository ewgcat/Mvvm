package com.lishuaihua.baselib.binding.base

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.lishuaihua.baselib.binding.ext.observerWhenCreated
import com.lishuaihua.baselib.binding.ext.observerWhenDestroyed
import kotlin.properties.ReadOnlyProperty


abstract class FragmentDelegate<T : ViewBinding>(
    fragment: Fragment
) : ReadOnlyProperty<Fragment, T> {

    protected var viewBinding: T? = null

    init {
        fragment.lifecycle.observerWhenCreated {
            fragment.viewLifecycleOwnerLiveData.observe(fragment) { viewOwner ->
                viewOwner.lifecycle.observerWhenDestroyed {
                    destroyed()
                }
            }
        }

    }

    private fun destroyed() {
        viewBinding = null
    }
}