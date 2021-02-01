package com.lishuaihua.baselib.base


import androidx.databinding.ViewDataBinding
import com.lishuaihua.net.httputils.BaseViewModel

abstract class MvvmBaseActivity<U : BaseViewModel, V : ViewDataBinding>() :
    BaseViewModelActivity<U>() {



}


