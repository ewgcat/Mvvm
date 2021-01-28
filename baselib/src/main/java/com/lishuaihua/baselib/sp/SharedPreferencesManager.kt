package com.lishuaihua.baselib.sp

import android.content.Context
import android.content.SharedPreferences

/**
 * 共享参数管理类
 */
class SharedPreferencesManager private constructor(context: Context) {
    private var sp: SharedPreferences? = null

    /**
     * 保存一个boolean值到SharedPreferences中
     */
    fun saveBoolean(key: String?, value: Boolean) {
        sp!!.edit().putBoolean(key, value).commit()
    }

    /**
     * 从SharedPreferences取出一个boolean类型 Value值
     */
    fun getBoolean(key: String?, defValue: Boolean): Boolean {
        return sp!!.getBoolean(key, defValue)
    }

    /**
     * 从SharedPreferences取出一个字符串Value值
     */
    fun getString(key: String?, defValue: String?): String? {
        return sp!!.getString(key, defValue)
    }

    /**
     * 保存一个字符串值到SharedPreferences中
     */
    fun saveString(key: String?, value: String?) {
        sp!!.edit().putString(key, value).commit()
    }

    fun clearString() {
        sp!!.edit().clear().commit()
    }

    fun saveInt(key: String?, value: Int) {
        sp!!.edit().putInt(key, value).commit()
    }

    fun getInt(key: String?, defValue: Int): Int {
        return sp!!.getInt(key, defValue)
    }

    fun saveLong(key: String?, value: Long) {
        sp!!.edit().putLong(key, value).commit()
    }

    fun getLong(key: String?, defValue: Long): Long {
        return sp!!.getLong(key, defValue)
    }

    fun saveSet(key: String?, value: Set<String?>?) {
        sp!!.edit().putStringSet(key, value).commit()
    }

    fun getSet(key: String?, defValue: Set<String?>?): Set<String>? {
        return sp!!.getStringSet(key, defValue)
    }

    /**
     * 操作SharedPreference相关常量
     */
    interface SPCommons {
        companion object {
            const val CART_GOODS_NUM = "cart_goods_num"

            //是否为第一次安装app
            //用户token
            const val TOKEN = "token"

            //设备唯一识别号
            const val IMEI = "imei"

            //会话id
            const val SESSIONID = "sessionId"
            const val ISSHOWWMINEFRAGMENTDATA = "is_show_minefragment_data"
            const val ISSHOWPROFIT = "is_show_profit"
            const val ISSHOWORDERPROFIT = "is_show_order_profit"
        }
    }

    companion object {
        private var INSTANCE: SharedPreferencesManager? = null
        fun getInstance(context: Context): SharedPreferencesManager? {
            if (INSTANCE == null) {
                synchronized(SharedPreferencesManager::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = SharedPreferencesManager(context)
                    }
                }
            }
            return INSTANCE
        }
    }

    init {
        if (sp == null) {
            sp = context.getSharedPreferences("com.lishuaihua.mvvmframework", 0)
        }
    }
}