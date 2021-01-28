package com.lishuiahua.webview.command;

import android.content.Context;

import java.util.Map;

public interface Command {
    String COMMAND_UPDATE_TITLE = "lishuaihua_webview_update_title";
    String COMMAND_UPDATE_TITLE_PARAMS_TITLE = "lishuaihua_webview_update_title_params_title";

    String name();

    void exec(Context context, Map params, ResultBack resultBack);
}
