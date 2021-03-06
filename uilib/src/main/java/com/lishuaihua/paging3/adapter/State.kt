package com.lishuaihua.paging3.adapter

/**
 * @description : Adapter 刷新或加载更多状态
 */
sealed class State {
    object Loading : State()
    class Success(val noMoreData: Boolean) : State()
    object Error : State()
}