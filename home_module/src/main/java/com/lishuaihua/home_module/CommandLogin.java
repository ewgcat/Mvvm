package com.lishuaihua.home_module;

import android.os.RemoteException;
import android.util.Log;

import com.google.auto.service.AutoService;
import com.google.gson.Gson;
import com.lishuaihua.baselib.autoservice.IUserCenterService;
import com.lishuaihua.baselib.autoservice.ServiceLoaderHelper;
import com.lishuaihua.baselib.eventbus.LoginEvent;
import com.lishuaihua.webview.ICallbackFromMainprocessToWebViewProcessInterface;
import com.lishuaihua.webview.command.Command;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

@AutoService({Command.class})
public class CommandLogin implements Command {
    IUserCenterService iUserCenterService = ServiceLoaderHelper.load(IUserCenterService.class);
    ICallbackFromMainprocessToWebViewProcessInterface callback;
    String callbacknameFromNativeJs;
    public CommandLogin(){
        EventBus.getDefault().register(this);
    }

    @Override
    public String name() {
        return "login";
    }

    @Override
    public void execute(final Map parameters, ICallbackFromMainprocessToWebViewProcessInterface callback) {
        Log.d("CommandLogin", new Gson().toJson(parameters));
        if (iUserCenterService != null && !iUserCenterService.isLogined()) {
            iUserCenterService.login();
            this.callback = callback;
            this.callbacknameFromNativeJs = parameters.get("callbackname").toString();
        }
    }

    @Subscribe
    public void onMessageEvent(LoginEvent event) {
        Log.d("CommandLogin", event.userName);
        HashMap map = new HashMap();
        map.put("accountName", event.userName);
        if(this.callback != null) {
            try {
                this.callback.onResult(callbacknameFromNativeJs, new Gson().toJson(map));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
