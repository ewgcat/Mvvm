package com.gialen.baselib.util

import java.text.DecimalFormat

object MoneyFomatUtil {
    /**
     * 将double 数值转为2位小数的字符串
     * @param money
     * @return
     */
    fun fomat2PointNumString(money: Double?): String {
        val fnum = DecimalFormat("##0.00")
        return fnum.format(money)
    }

    /**
     * 将double 数值转为2位小数的数值
     * @param money
     * @return
     */
    fun fomat2PointNumDouble(money: Double?): Double {
        val fnum = DecimalFormat("##0.00")
        val format = fnum.format(money)
        return format.toDouble()
    }
}