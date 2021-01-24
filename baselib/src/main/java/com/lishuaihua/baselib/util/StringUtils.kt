package com.lishuaihua.baselib.util

import java.io.UnsupportedEncodingException
import java.net.URLEncoder

/**
 * 字符串工具类
 */
object StringUtils {
    /**
     * 判断字符串是否有值
     */
    @JvmStatic
    fun isEmpty(str: String?): Boolean {
        if (str == null) {
            return true
        } else if ("" == str.trim { it <= ' ' } || "null" == str.trim { it <= ' ' }) {
            return true
        }
        return false
    }

    /**
     * Get XML String of utf-8
     *
     * @return XML-Formed string
     */
    fun getUTF8XMLString(xml: String?): String {
        // A StringBuffer Object
        val sb = StringBuffer()
        sb.append(xml)
        var xmString = ""
        var xmlUTF8 = ""
        try {
            xmString = String(sb.toString().toByteArray(charset("UTF-8")))
            xmlUTF8 = URLEncoder.encode(xmString, "UTF-8")
            println("utf-8 编码：$xmlUTF8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        // return to String Formed
        return xmlUTF8
    }
}