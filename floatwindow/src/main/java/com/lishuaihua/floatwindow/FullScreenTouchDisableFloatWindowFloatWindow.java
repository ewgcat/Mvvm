package com.lishuaihua.floatwindow;

import android.content.Context;



public class FullScreenTouchDisableFloatWindowFloatWindow extends BaseFloatWindow {

    public FullScreenTouchDisableFloatWindowFloatWindow(Context context) {
        super(context);
    }

    @Override
    public void create() {
        super.create();

        mViewMode = FULLSCREEN_NOT_TOUCHABLE;

        inflate(R.layout.main_layout_float_full_screen_touch_disable);


    }

    @Override
    protected void onAddWindowFailed(Exception e) {

    }
}
