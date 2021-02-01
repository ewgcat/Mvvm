package com.lishuaihua.webview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.lishuaihua.R
import com.lishuaihua.baselib.loadsir.ErrorCallback
import com.lishuaihua.baselib.loadsir.LoadingCallback
import com.lishuaihua.databinding.FragmentWebviewBinding
import com.lishuaihua.webview.utils.Constants
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener

class WebViewFragment : Fragment(), WebViewCallBack, OnRefreshListener {
    private var mUrl: String? = null
    private var mBinding: FragmentWebviewBinding? = null
    private var mLoadService: LoadService<*>? = null
    private var mCanNativeRefresh = false
    private var mIsError = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            mUrl = bundle.getString(Constants.URL)
            mCanNativeRefresh = bundle.getBoolean(Constants.CAN_NATIVE_REFRESH)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_webview, container, false)
        mBinding!!.webview.registerWebViewCallBack(this)
        mBinding!!.webview.loadUrl(mUrl!!)
        mLoadService = LoadSir.getDefault().register(mBinding!!.smartrefreshlayout) {
            mLoadService!!.showCallback(LoadingCallback::class.java)
            mBinding!!.webview.reload()
        }
        mBinding!!.smartrefreshlayout.setOnRefreshListener(this)
        mBinding!!.smartrefreshlayout.setEnableRefresh(mCanNativeRefresh)
        mBinding!!.smartrefreshlayout.setEnableLoadMore(false)
        return mLoadService!!.getLoadLayout()
    }

    override fun pageStarted(url: String) {
        if (mLoadService != null) {
            mLoadService!!.showCallback(LoadingCallback::class.java)
        }
    }

    override fun pageFinished(url: String) {
        if (mIsError) {
            mBinding!!.smartrefreshlayout.setEnableRefresh(true)
        } else {
            mBinding!!.smartrefreshlayout.setEnableRefresh(mCanNativeRefresh)
        }
        Log.d(TAG, "pageFinished")
        mBinding!!.smartrefreshlayout.finishRefresh()
        if (mLoadService != null) {
            if (mIsError) {
                mLoadService!!.showCallback(ErrorCallback::class.java)
            } else {
                mLoadService!!.showSuccess()
            }
        }
        mIsError = false
    }

    override fun onError() {
        Log.e(TAG, "onError")
        mIsError = true
        mBinding!!.smartrefreshlayout.finishRefresh()
    }

    override fun updateTitle(title: String) {
        if (activity is WebViewActivity) {
            (activity as WebViewActivity?)!!.updateTitle(title)
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        mBinding!!.webview.reload()
    }

    companion object {
        private const val TAG = "WebViewFragment"
        @JvmStatic
        fun newInstance(url: String?, canNativeRefresh: Boolean): WebViewFragment {
            val fragment = WebViewFragment()
            val bundle = Bundle()
            bundle.putString(Constants.URL, url)
            bundle.putBoolean(Constants.CAN_NATIVE_REFRESH, canNativeRefresh)
            fragment.arguments = bundle
            return fragment
        }
    }
}