package com.lishuaihua.login_module;

import android.content.Intent;

import com.google.auto.service.AutoService;
import com.lishuaihua.baselib.autoservice.IUserCenterService;
import com.lishuaihua.baselib.base.BaseApplication;

@AutoService({IUserCenterService.class})
public class IUserCenterServiceImpl implements IUserCenterService {
    @Override
    public boolean isLogined() {
        return false;
    }

    @Override
    public void login() {
        Intent intent = new Intent(BaseApplication.instance, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        BaseApplication.instance.startActivity(intent);
    }
}
