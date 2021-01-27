package com.lishuiahua.webview_module

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import com.lishuaihua.baselib.base.BaseActivity
import com.lishuaihua.baselib.binding.binding
import com.lishuaihua.net.httputils.BaseViewModel
import com.lishuiahua.webview.AccountWebFragment
import com.lishuiahua.webview.CommonWebFragment
import com.lishuiahua.webview.basefragment.BaseWebviewFragment
import com.lishuiahua.webview.command.Command
import com.lishuiahua.webview.command.CommandsManager
import com.lishuiahua.webview.command.ResultBack
import com.lishuiahua.webview.utils.WebConstants
import com.lishuiahua.webview_module.databinding.ActivityWebBinding
import java.util.*

class WebActivity : BaseActivity<BaseViewModel>() {
    private var title: String? = null
    private var url: String? = null
    var webviewFragment: BaseWebviewFragment? = null
    private val binding: ActivityWebBinding by binding()
    override fun getLayoutResId(): Int =R.layout.activity_web
    override fun doCreateView(savedInstanceState: Bundle?) {
        title = intent.getStringExtra(WebConstants.INTENT_TAG_TITLE)
        url = intent.getStringExtra(WebConstants.INTENT_TAG_URL)
        binding.navigationBar.setTitle(title!!)
        binding.navigationBar.setBackClickListener(this)
        val fm = supportFragmentManager
        val transaction = fm.beginTransaction()
        CommandsManager.getInstance().registerCommand(WebConstants.LEVEL_LOCAL, titleUpdateCommand)
        val level = intent.getIntExtra("level", WebConstants.LEVEL_BASE)
        webviewFragment = null
        webviewFragment = if (level == WebConstants.LEVEL_BASE) {
            CommonWebFragment.newInstance(url)
        } else {
            AccountWebFragment.newInstance(
                url!!,
                (intent.extras!!.getSerializable(WebConstants.INTENT_TAG_HEADERS) as HashMap<String, String>)!!,
                true
            )
        }
        transaction.replace(R.id.web_view_fragment, webviewFragment!!).commit()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (webviewFragment != null && webviewFragment is BaseWebviewFragment) {
            val flag = webviewFragment!!.onKeyDown(keyCode, event)
            if (flag) {
                return flag
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    /**
     * 页面路由
     */
    private val titleUpdateCommand: Command = object : Command {
        override fun name(): String {
            return Command.COMMAND_UPDATE_TITLE
        }

        override fun exec(context: Context, params: Map<*, *>, resultBack: ResultBack) {
            if (params.containsKey(Command.COMMAND_UPDATE_TITLE_PARAMS_TITLE)) {
                setTitle(params[Command.COMMAND_UPDATE_TITLE_PARAMS_TITLE] as String?)
            }
        }
    }

    companion object {
        fun startCommonWeb(context: Context, title: String?, url: String?, testLevel: Int) {
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra(WebConstants.INTENT_TAG_TITLE, title)
            intent.putExtra(WebConstants.INTENT_TAG_URL, url)
            intent.putExtra("level", testLevel)
            if (context is Service) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }

        fun startAccountWeb(
            context: Context,
            title: String?,
            url: String?,
            testLevel: Int,
            headers: HashMap<String, String>
        ) {
            val intent = Intent(context, WebActivity::class.java)
            val bundle = Bundle()
            bundle.putString(WebConstants.INTENT_TAG_TITLE, title)
            bundle.putString(WebConstants.INTENT_TAG_URL, url)
            bundle.putSerializable(WebConstants.INTENT_TAG_HEADERS, headers)
            bundle.putInt("level", testLevel)
            if (context is Service) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

}