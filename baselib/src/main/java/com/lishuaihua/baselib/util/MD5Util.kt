package com.gialen.baselib.util

import java.security.MessageDigest

object MD5Util {
    fun MD5(s: String): String {
        val hexDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')
        return try {
            val btInput = s.toByteArray(charset("UTF-8"))
            // 获得MD5摘要算法的 MessageDigest 对象
            val mdInst = MessageDigest.getInstance("MD5")
            // 使用指定的字节更新摘要
            mdInst.update(btInput)
            val data = mdInst.digest()
            val r = StringBuilder(data.size * 2)
            data.forEach { b ->
                val i = b.toInt()
                r.append(hexDigits[i shr 4 and 0xF])
                r.append(hexDigits[i and 0xF])
            }
            r.toString()
        } catch (e: Exception) {
            ""
        }
    }

    fun md5(s: String): String {
        val hexDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
        return try {
            val btInput = s.toByteArray(charset("UTF-8"))
            // 获得MD5摘要算法的 MessageDigest 对象
            val mdInst = MessageDigest.getInstance("MD5")
            // 使用指定的字节更新摘要
            mdInst.update(btInput)
            // 获得密文
            val data = mdInst.digest()
            val r = StringBuilder(data.size * 2)
            data.forEach { b ->
                val i = b.toInt()
                r.append(hexDigits[i shr 4 and 0xF])
                r.append(hexDigits[i and 0xF])
            }
            r.toString()
        } catch (e: Exception) {
            ""
        }
    }





}