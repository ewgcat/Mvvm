package com.lishuaihua.net.httputils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lishuaihua.net.error.ErrorResult
import com.lishuaihua.net.error.ErrorUtil
import com.lishuaihua.net.response.BaseResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject


open class BaseViewModel : ViewModel() {


    var isShowLoading = MutableLiveData<Boolean>()//是否显示loading
    var errorData = MutableLiveData<ErrorResult>()//错误信息

    private fun showLoading() {
        isShowLoading.value = true
    }

    private fun dismissLoading() {
        isShowLoading.value = false
    }

    private fun showError(error: ErrorResult) {
        errorData.value = error
    }

    private fun error(errorResult: ErrorResult) {
        showError(ErrorResult(errorResult.code, errorResult.msg))
    }

    /**
     * 注意此方法传入的参数：api是以函数作为参数传入
     * api：即接口调用方法
     * error：可以理解为接口请求失败回调
     * ->数据类型，表示方法返回该数据类型
     * ->Unit，表示方法不返回数据类型
     */
    fun <T> launch(
        api: suspend CoroutineScope.() -> BaseResult<T>,//请求接口方法，T表示data实体泛型，调用时可将data对应的bean传入即可
        liveData: MutableLiveData<T>,
        isShowLoading: Boolean = false
    ) {
        if (isShowLoading) showLoading()
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {//异步请求接口
                    val result = api()
                    withContext(Dispatchers.Main) {
                        when(result.code){
                            200->{//请求成功
                                liveData.value = result.data
                            }
                            4001->{

                            }
                            else ->{
                                error(ErrorResult(result.code, result.msg))
                            }
                        }
                    }
                }
            } catch (e: Throwable) {//接口请求失败
                error(ErrorUtil.getError(e))
            } finally {//请求结束
                dismissLoading()
            }
        }
    }
    /**
     * 封装必传参数
     * @param data 请求参数
     */
    protected fun getBaseParams(data: JSONObject?): RequestBody {
        return data.toString().toRequestBody("application/json".toMediaTypeOrNull())

    }

    /**
     * 封装必传参数
     * @param data 请求参数
     */
    protected fun getBaseParams(data: JSONArray?): RequestBody {
        return data.toString().toRequestBody("application/json".toMediaTypeOrNull())
    }

}

