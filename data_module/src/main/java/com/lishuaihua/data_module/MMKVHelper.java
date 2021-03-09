package com.lishuaihua.data_module;

import android.content.Context;

import com.tencent.mmkv.MMKV;

public class MMKVHelper {


    public static void init(Context context){
        String dir = context.getFilesDir().getAbsolutePath() + "/mmkv_2";
        MMKV.initialize(dir);
    }

    public static MMKV getdefaultMMKV(){
        return  MMKV.defaultMMKV();
    }

    public static MMKV getSingleMMKVWithID(String id){
        return MMKV.mmkvWithID(id);
    }

    public static MMKV getMultiProcessMMKVWithID(String id){
        return MMKV.mmkvWithID(id,MMKV.MULTI_PROCESS_MODE);
    }


 }
