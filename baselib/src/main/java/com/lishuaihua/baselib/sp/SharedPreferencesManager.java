package com.lishuaihua.baselib.sp;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * 共享参数管理类
 */
public class SharedPreferencesManager {

    private SharedPreferences sp;
    private static SharedPreferencesManager INSTANCE;

    private SharedPreferencesManager(Context context) {
        if (sp == null) {
            this.sp = context.getSharedPreferences("com.lishuaihua.mvvmframework", 0);
        }
    }

    public static SharedPreferencesManager getInstance(Context context){
        if(INSTANCE == null){
            synchronized (SharedPreferencesManager.class){
                if(INSTANCE == null){
                    INSTANCE = new SharedPreferencesManager(context);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 保存一个boolean值到SharedPreferences中
     */
    public void saveBoolean(String key, boolean value) {
        sp.edit().putBoolean(key, value).commit();
    }

    /**
     * 从SharedPreferences取出一个boolean类型 Value值
     */
    public boolean getBoolean(String key, boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    /**
     * 从SharedPreferences取出一个字符串Value值
     */
    public String getString(String key, String defValue) {
        return sp.getString(key, defValue);
    }

    /**
     * 保存一个字符串值到SharedPreferences中
     */
    public void saveString(String key, String value) {
        sp.edit().putString(key, value).commit();
    }
    public void clearString(){
        sp.edit().clear().commit();
    }

    public void saveInt(String key, int value) {
        sp.edit().putInt(key, value).commit();
    }

    public int getInt(String key, int defValue) {
        return sp.getInt(key, defValue);
    }

    public void saveLong(String key, long value) {
        sp.edit().putLong(key, value).commit();
    }

    public long getLong(String key, long defValue) {
        return sp.getLong(key, defValue);
    }

    public void saveSet(String key, Set<String> value) {
        sp.edit().putStringSet(key, value).commit();
    }

    public Set<String> getSet(String key , Set<String> defValue) {
        return sp.getStringSet(key,defValue);
    }

    /**
     * 操作SharedPreference相关常量
     */
    public interface SPCommons{
        String CART_GOODS_NUM = "cart_goods_num";
        //是否为第一次安装app
        //用户token
        String TOKEN = "token";
        //设备唯一识别号
        String IMEI = "imei";
        //会话id
        String SESSIONID = "sessionId";
        String ISSHOWWMINEFRAGMENTDATA = "is_show_minefragment_data";
        String ISSHOWPROFIT = "is_show_profit";
        String ISSHOWORDERPROFIT = "is_show_order_profit";

    }
}

