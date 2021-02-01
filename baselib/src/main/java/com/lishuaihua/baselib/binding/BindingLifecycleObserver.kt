package com.lishuaihua.binding

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner


 abstract class BindingLifecycleObserver : LifecycleObserver {

   abstract fun onCreate(owner: LifecycleOwner)

     fun onStart(owner: LifecycleOwner) {
    }

    fun onResume(owner: LifecycleOwner) {
    }

    fun onPause(owner: LifecycleOwner) {
    }

    fun onStop(owner: LifecycleOwner) {
    }

    abstract  fun onDestroy(owner: LifecycleOwner)

}