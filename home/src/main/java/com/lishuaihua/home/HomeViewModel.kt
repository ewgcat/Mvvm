package com.lishuaihua.home

import androidx.lifecycle.viewModelScope
import com.lishuaihua.data_module.model.User
import com.lishuaihua.data_module.room.RoomDataManager
import com.lishuaihua.net.httputils.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : BaseViewModel(){
    fun insertUser(user: User){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                RoomDataManager.getInstance().insertUser(user)
            }
        }
    }
}