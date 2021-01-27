package com.lishuiahua.webview.command;

import com.lishuiahua.webview.utils.WebConstants;

public class BaseLevelCommands extends Commands {

    public BaseLevelCommands() {
    }

    @Override
    int getCommandLevel() {
        return WebConstants.LEVEL_BASE;
    }
}
