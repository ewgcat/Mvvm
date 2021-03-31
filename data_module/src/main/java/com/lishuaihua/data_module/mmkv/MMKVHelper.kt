package com.lishuaihua.data_module.mmkv

import android.content.Context
import android.content.SharedPreferences
import com.tencent.mmkv.MMKV

object MMKVHelper {
    var migrateSp: SpProxy? = null
    var sigleMigrateSp: SpProxy? = null
    var MultiProcessmigrateSp: SpProxy? = null
    var defaultMMKV: MMKV? = null
    var singleMMKVWithID: MMKV? = null
    var multiProcessMMKVWithID: MMKV? = null
    fun init(context: Context) {
        if (migrateSp == null) {
            val dir = context.filesDir.absolutePath + "/mmkv_2"
            MMKV.initialize(dir)
            defaultMMKV = getdefaultMMKV()
            migrateSp = SpProxy(defaultMMKV)
        }
    }

    fun getdefaultMMKV(): MMKV? {
        return MMKV.defaultMMKV()
    }

    fun initSingleMMKVWithID(context: Context, id: String?) {
        init(context)
        singleMMKVWithID = MMKV.mmkvWithID(id)
        sigleMigrateSp = SpProxy(singleMMKVWithID)
    }

    fun initMultiProcessMMKVWithID(context: Context, id: String?) {
        init(context)
        multiProcessMMKVWithID = MMKV.mmkvWithID(id, MMKV.MULTI_PROCESS_MODE)
        MultiProcessmigrateSp = SpProxy(multiProcessMMKVWithID)
    }

    fun migrate(preferences: SharedPreferences) {
        val kvs = preferences.all
        if (kvs != null && kvs.isNotEmpty()) {
            val iterator = kvs.entries.iterator()
            while (iterator.hasNext()) {
                val entry = iterator.next()
                val key = entry.key
                val value = entry.value
                if (key != null && value != null) {
                    migrateSp.run {
                        when (value) {
                            is Boolean -> this?.putBoolean(key, value)
                            is Int -> this?.putInt(key, value)
                            is Long -> this?.putLong(key, value)
                            is Float -> this?.putFloat(key, value)
                            is String -> this?.putString(key, value)
                            else -> {
                            }
                        }
                    }
                }
            }
            kvs.size
        }
    }

    /**
     * 读取Int
     */
    fun getInt(key: String?, defValue: Int): Int {
        return migrateSp!!.getInt(key, defValue)
    }

    /**
     * 读取Long
     */
    fun getLong(key: String?, defValue: Long): Long {
        return migrateSp!!.getLong(key, defValue)
    }

    /**
     * 读取Float
     */
    fun getFloat(key: String?, defValue: Float): Float {
        return migrateSp!!.getFloat(key, defValue)
    }

    /**
     * 读取Boolean
     */
    fun getBoolean(key: String?, defValue: Boolean): Boolean {
        return migrateSp!!.getBoolean(key, defValue)
    }

    /**
     * 读取String
     */
    fun getString(key: String?, defValue: String?): String? {
        return migrateSp!!.getString(key, defValue)
    }

    /**
     * 读取StringSet
     */
     fun getStringSet(key: String?, defValues: MutableSet<String>?): MutableSet<String> {
        return migrateSp!!.getStringSet(key, defValues)
    }

}