package com.lishuaihua.mvvmframework

import androidx.lifecycle.MutableLiveData
import com.lishuaihua.baselib.base.BaseViewModel

class MainViewModel : BaseViewModel(){
     var text : MutableLiveData<String> = MutableLiveData<String>()
    init {
        text.value="text"
    }
}