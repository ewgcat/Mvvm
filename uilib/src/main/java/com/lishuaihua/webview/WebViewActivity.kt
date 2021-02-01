package com.lishuaihua.webview

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.lishuaihua.R
import com.lishuaihua.databinding.ActivityWebviewBinding
import com.lishuaihua.webview.WebViewFragment.Companion.newInstance
import com.lishuaihua.webview.utils.Constants

class WebViewActivity : AppCompatActivity() {
    private var mBinding: ActivityWebviewBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_webview)
        mBinding!!.title.text = intent.getStringExtra(Constants.TITLE)
        mBinding!!.actionBar.visibility = if (intent.getBooleanExtra(
                Constants.IS_SHOW_ACTION_BAR,
                true
            )
        ) View.VISIBLE else View.GONE
        mBinding!!.back.setOnClickListener { finish() }
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        val fragment: Fragment = newInstance(
            intent.getStringExtra(Constants.URL), true
        )
        transaction.replace(R.id.web_view_fragment, fragment).commit()
    }

    fun updateTitle(title: String?) {
        mBinding!!.title.text = title
    }
}