package com.lishuaihua.baselib.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class DateUtil {
    /**
     * 获取系统时间戳
     *
     * @return
     */
    val curTimeLong: Long
        get() = System.currentTimeMillis()

    companion object {
        /**
         * 获取当前日期
         *
         * @return
         */
        val currentDate: String
            get() {
                val df = SimpleDateFormat("yyyyMMdd")
                return df.format(Date())
            }

        /**
         * 获取明天日期
         *
         * @return
         */
        val tomorrowDate: String
            get() {
                val df = SimpleDateFormat("yyyyMMdd")
                return (Integer.valueOf(df.format(Date())) + 1).toString()
            }

        /**
         * 获取昨天
         *
         * @return
         */
        val preDate: String
            get() {
                val df = SimpleDateFormat("yyyyMMdd")
                return (Integer.valueOf(df.format(Date())) - 1).toString()
            }

        /**
         * 获取当前日期字符串
         *
         * @return
         */
        val currentDateString: String
            get() {
                val df = SimpleDateFormat("yyyy年MM月dd日")
                return df.format(Date())
            }

        /**
         * 获取当前日期字符串
         *
         * @return
         */
        val currentTimeString: String
            get() {
                val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                return df.format(Date())
            }

        /**
         * 获取当前年
         *
         * @return
         */
        val currentYear: Int
            get() {
                val cal = Calendar.getInstance()
                return cal[Calendar.YEAR]
            }

        /**
         * 获取当前月
         *
         * @return
         */
        val currentMonth: Int
            get() {
                val cal = Calendar.getInstance()
                return cal[Calendar.MONTH]
            }

        /**
         * 获取当前日
         *
         * @return
         */
        val currentDay: Int
            get() {
                val cal = Calendar.getInstance()
                return cal[Calendar.DATE]
            }

        fun dateBeforeCurrentDate(time: String): Boolean {
            var b = false
            val currentDate = currentDate
            val df = SimpleDateFormat("yyyyMMdd")
            try {
                b = if (time == currentDate) {
                    return true
                } else {
                    val date = df.parse(time)
                    val currentdate = df.parse(currentDate)
                    val i = date.compareTo(currentdate)
                    if (i > 0) {
                        false
                    } else {
                        true
                    }
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return b
        }

        /**
         * 切割标准时间
         *
         * @param time
         * @return
         */
        fun subStandardTime(time: String): String? {
            val idx = time.indexOf(".")
            return if (idx > 0) {
                time.substring(0, idx).replace("T", " ")
            } else null
        }

        /**
         * 将时间戳转化为字符串
         *
         * @param showTime
         * @return
         */
        @JvmOverloads
        fun formatTime2String(showTime: Long, haveYear: Boolean = false): String {
            var str = ""
            val distance = System.currentTimeMillis() / 1000 - showTime
            if (distance < 300) {
                str = "刚刚"
            } else if (distance >= 300 && distance < 600) {
                str = "5分钟前"
            } else if (distance >= 600 && distance < 1200) {
                str = "10分钟前"
            } else if (distance >= 1200 && distance < 1800) {
                str = "20分钟前"
            } else if (distance >= 1800 && distance < 2700) {
                str = "半小时前"
            } else if (distance >= 2700) {
                val date = Date(showTime * 1000)
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                str = formatDateTime(sdf.format(date), haveYear)
            }
            return str
        }

        fun formatDate2String(time: String?): String {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            if (time == null) {
                return "未知"
            }
            try {
                val createTime = format.parse(time).time / 1000
                val currentTime = System.currentTimeMillis() / 1000
                return if (currentTime - createTime - 24 * 3600 > 0) { //超出一天
                    (currentTime - createTime).div(24 * 3600).toString() + "天前"
                } else {
                    (currentTime - createTime).div(3600).toString() + "小时前"
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return "未知"
        }

        fun formatDateTime(time: String?, haveYear: Boolean): String {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            if (time == null) {
                return ""
            }
            val date: Date
            date = try {
                format.parse(time)
            } catch (e: ParseException) {
                e.printStackTrace()
                return ""
            }
            val current = Calendar.getInstance()
            val today = Calendar.getInstance()
            today[Calendar.YEAR] = current[Calendar.YEAR]
            today[Calendar.MONTH] = current[Calendar.MONTH]
            today[Calendar.DAY_OF_MONTH] = current[Calendar.DAY_OF_MONTH]
            today[Calendar.HOUR_OF_DAY] = 0
            today[Calendar.MINUTE] = 0
            today[Calendar.SECOND] = 0
            val yesterday = Calendar.getInstance()
            yesterday[Calendar.YEAR] = current[Calendar.YEAR]
            yesterday[Calendar.MONTH] = current[Calendar.MONTH]
            yesterday[Calendar.DAY_OF_MONTH] = current[Calendar.DAY_OF_MONTH] - 1
            yesterday[Calendar.HOUR_OF_DAY] = 0
            yesterday[Calendar.MINUTE] = 0
            yesterday[Calendar.SECOND] = 0
            current.time = date
            return if (current.after(today)) {
                "今天 " + time.split(" ").toTypedArray()[1]
            } else if (current.before(today) && current.after(yesterday)) {
                "昨天 " + time.split(" ").toTypedArray()[1]
            } else {
                if (haveYear) {
                    val index = time.indexOf(" ")
                    time.substring(0, index)
                } else {
                    val yearIndex = time.indexOf("-") + 1
                    val index = time.indexOf(" ")
                    time.substring(yearIndex, time.length).substring(0, index)
                }
            }
        }

        /**
         * 获取当前时间
         *
         * @param pattern
         * @return
         */
        fun getCurDate(pattern: String?): String {
            val sDateFormat = SimpleDateFormat(pattern)
            return sDateFormat.format(Date())
        }

        /**
         * 根据输入的日期 获得当前日期为周几
         * @param dt
         * @return
         */
        fun getWeekOfDate(dt: Date?): String {
            val weekDays = arrayOf("周日", "周一", "周二", "周三", "周四", "周五", "周六")
            val cal = Calendar.getInstance()
            cal.time = dt
            var w = cal[Calendar.DAY_OF_WEEK] - 1
            if (w < 0) w = 0
            return weekDays[w]
        }

        /**
         * 时间戳转换成字符窜
         *
         * @param milSecond
         * @param pattern
         * @return
         */
        fun getDateToString(milSecond: Long, pattern: String?): String {
            val date = Date(milSecond)
            val format = SimpleDateFormat(pattern)
            return format.format(date)
        }

        /**
         * 时间戳转换成字符窜
         *
         * @param milSecond
         * @return
         */
        fun parseLongDateToDateString(milSecond: Long): String {
            val date = Date(milSecond)
            val format = SimpleDateFormat("yyyy-MM-dd")
            return format.format(date)
        }

        fun parseLongDateToTimeString(milSecond: Long): String {
            val date = Date(milSecond)
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            return format.format(date)
        }

        /**
         * 将字符串转为时间戳
         *
         * @param dateString
         * @param pattern
         * @return
         */
        fun getStringToDate(dateString: String?, pattern: String?): Long {
            val dateFormat = SimpleDateFormat(pattern)
            var date = Date()
            try {
                date = dateFormat.parse(dateString)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return date.time
        }

        /**
         * 获取10位时间戳
         * @return
         */
        val timestamp_10: Long
            get() {
                var time = timestamp_13.toString()
                time = time.substring(0, 10)
                return time.toLong()
            }

        /**
         * 获取11位时间戳
         * @return
         */
        val timestamp_11: Long
            get() {
                var time = timestamp_13.toString()
                time = time.substring(0, 11)
                return time.toLong()
            }

        /**
         * 获取13位时间戳
         * @return
         */
        val timestamp_13: Long
            get() = System.currentTimeMillis()

        /**
         * 通过时间戳解析出固定格式的时间字符串
         * @param dateFormat 时间格式"2017-07-07"
         * @param time
         * @return
         */
        fun parseDateFrom13(time: Long, dateFormat: String?): String {
            val date = Date(time)
            val format = SimpleDateFormat(dateFormat)
            return format.format(date)
        }

        /**
         * 时间戳转换成日期格式字符串
         *
         * @param seconds 精确到秒的字符串
         * @return
         */
        fun timeStamp2Date2(seconds: String?, format: String?): String {
            var format = format
            if (seconds == null || seconds.isEmpty() || seconds == "null") {
                return ""
            }
            if (format == null || format.isEmpty()) format = "yyyy-MM-dd"
            val sdf = SimpleDateFormat(format)
            return sdf.format(Date(java.lang.Long.valueOf(if (seconds.length == 10) seconds + "000" else seconds)))
        }
    }
}
