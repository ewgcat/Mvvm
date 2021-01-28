package com.lishuiahua.webview_module;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.lishuiahua.webview_module.R;
import com.lishuiahua.webview.AccountWebFragment;
import com.lishuiahua.webview.basefragment.BaseWebviewFragment;
import com.lishuiahua.webview.CommonWebFragment;
import com.lishuiahua.webview.command.Command;
import com.lishuiahua.webview.command.CommandsManager;
import com.lishuiahua.webview.command.ResultBack;
import com.lishuiahua.webview.utils.WebConstants;

import java.util.HashMap;
import java.util.Map;

public class WebActivity extends AppCompatActivity {
    private String title;
    private String url;

    BaseWebviewFragment webviewFragment;

    public static void startCommonWeb(Context context, String title, String url, int testLevel) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(WebConstants.INTENT_TAG_TITLE, title);
        intent.putExtra(WebConstants.INTENT_TAG_URL, url);
        intent.putExtra("level", testLevel);
        if (context instanceof Service) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }
    public static void startAccountWeb(Context context, String title, String url, int testLevel, @NonNull HashMap<String, String> headers) {
        Intent intent = new Intent(context, WebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(WebConstants.INTENT_TAG_TITLE, title);
        bundle.putString(WebConstants.INTENT_TAG_URL, url);
        bundle.putSerializable(WebConstants.INTENT_TAG_HEADERS, headers);
        bundle.putInt("level", testLevel);
        if (context instanceof Service) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        title = getIntent().getStringExtra(WebConstants.INTENT_TAG_TITLE);
        url = getIntent().getStringExtra(WebConstants.INTENT_TAG_URL);
        setTitle(title);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        CommandsManager.getInstance().registerCommand(WebConstants.LEVEL_LOCAL, titleUpdateCommand);
        int level = getIntent().getIntExtra("level", WebConstants.LEVEL_BASE);
        webviewFragment = null;
        if (level == WebConstants.LEVEL_BASE) {
            webviewFragment = CommonWebFragment.newInstance(url);
        } else {
            webviewFragment = AccountWebFragment.newInstance(url, (HashMap<String, String>) getIntent().getExtras().getSerializable(WebConstants.INTENT_TAG_HEADERS), true);
        }
        transaction.replace(R.id.web_view_fragment, webviewFragment).commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (webviewFragment != null && webviewFragment instanceof BaseWebviewFragment) {
            boolean flag = webviewFragment.onKeyDown(keyCode, event);
            if (flag) {
                return flag;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 页面路由
     */
    private final Command titleUpdateCommand = new Command() {
        @Override
        public String name() {
            return Command.COMMAND_UPDATE_TITLE;
        }

        @Override
        public void exec(Context context, Map params, ResultBack resultBack) {
            if(params.containsKey(Command.COMMAND_UPDATE_TITLE_PARAMS_TITLE)) {
                setTitle((String)params.get(Command.COMMAND_UPDATE_TITLE_PARAMS_TITLE));
            }
        }
    };

}
