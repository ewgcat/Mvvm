package com.lishuaihua.servicemanager.service

import androidx.annotation.IntDef
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

interface ISettingService {
    @IntDef(
        CODE_NO_NOTIFY_CHANGE,
        CODE_LANGUAGE,
        CODE_THEME,
        CODE_HOLDING_SHOW,
        CODE_FOREGROUND_REFRESH_RATE,
        CODE_FONT_SCHEME,
        CODE_COLOR_SCHEME,
        CODE_NAME_SYMBOL,
        CODE_DEFAULT_CHART,
        CODE_ORDER_BY,
        CODE_PORTFOLIO_LISTING,
        CODE_NOTIFICATION_ENABLE,
        CODE_NOTIFICATION_PERIOD,
        CODE_GOOGLE_FINANCE_SYNC,
        CODE_MARKET_REGION,
        CODE_GOOGLE_TOKEN_REFRESH,
        CODE_AUTO_SWITCH_DAILY_CHART,
        CODE_NEWS_OPEN_BROWSER,
        CODE_ENABLE_GENERAL
    )
    @Retention(
        RetentionPolicy.SOURCE
    )
    annotation class SettingCode

    /***
     * 注册配置信息改变
     * @param code
     * @param changeListener
     */
    fun registerListener(code: Int, changeListener: OnSettingPreferenceChangeListener?)
    /***
     * 获取语言
     * @return
     */
    /**
     * 设置语言
     */
    var language: String?

    /***
     * 获取主题
     * @return 0:暗色  1.亮色  2.纯黑
     */
    fun getThemeValue(): Int
    fun setThemeValue(theme: String?)

    //开启智能换肤
    val isOpenChangeOnTime: Boolean

    /***
     * 获取字体
     * @return 0:大  1.中  2.小
     */
    val fontSchemeValue: Int
    val newFontSchemeValue: Int

    /***
     * 获取配色方案
     * @return 0:涨：绿色，跌：红色  1：涨：红色，跌：绿色  2：没有颜色 3： 涨：绿色，跌：黄色
     */
    val colorSchemeValue: Int

    /***
     * 获取股票显示方式
     * @return false:代码，名称  true：名称，代码
     */
    val isNameSymbol: Boolean

    /***
     * 获取股票显示方式
     * @return 0:代码，名称  1：名称，代码
     */
    fun getNameSymbol(): String?

    /***
     * 获取默认图表
     * @return
     * 英文： 1d:One Day(Default)，  5d:Five Days， 3M：Three Months，  1Y:One Year， 5Y：Five Years  40Y：Max
     * 简体：1d:1日 ,5d：5日, Daily:日K, Weekily:周K,  Monthily:月K
     */
    val defaultChart: String?

    /***
     * 获取默认排序
     * @return 0:拖动排序(默认), 1：名称, 2:代码, 3:涨幅, 4:跌幅 5:振幅
     */
    val orderValue: Int

    /***
     * 获取默认排序
     * @return 1:优先展示涨跌幅, 2：同时展示涨跌幅与涨跌额
     */
    val portfolioStyleValue: Int

    /**
     * 获取刷新频率
     *
     * @return 2： 表示手动刷新  1：表示实时推送  5000:5秒  10000: 10秒  20000:20秒  30000：30秒  60000：60秒
     */
    val foregroundRefreshRate: Int

    /**
     * 是否启动谷歌财经同步
     *
     * @return
     */
    val isGoogleFinanceSyncEnable: Boolean

    /**
     * 获取谷歌账号
     */
    val googleAccount: String?

    /***
     * 是否在浏览器中打开新闻
     * @return
     */
    val isOpenNewsByBrowserEnable: Boolean

    /**
     * 获取TOKEN
     */
    val googleToken: String?

    /**
     * 初始化对Google的接口监听
     */
    fun initGoolgeFinanceExpireListener()

    /**
     * 刷新谷歌同步
     */
    fun refreshGoogleToken()

    /**
     * 是否显示持仓
     *
     * @return
     */
    val isShowHolding: Boolean

    /***
     * 是否显示通知
     * @return
     */
    val isNotificationEnable: Boolean

    /***
     * 是否启动通知振动
     * @return
     */
    val isNotificationVibrateEnable: Boolean

    /***
     * 获取铃声
     * @return
     */
    val notificationRingtone: String?

    /**
     * 获取提醒策略
     *
     * @return
     */
    val notificationPeriodValue: Int

    /***
     * 获取提醒开始时间
     * @return
     */
    val notificationStartTime: String?

    /***
     * 获取提醒结束时间
     * @return
     */
    val notificationEndTime: String?

    /**
     * 用户同步设置
     */
    fun syncUserSetting()

    /***
     * 用户上传设置
     */
    fun updateUserSetting()

    /**
     * 是自动切换分时图
     */
    var isAutoSwitchDailyChart: Boolean

    interface OnSettingPreferenceChangeListener {
        fun onPreferenceChange(code: Int)
    }

    fun initTextFontWithSystem(fontScale: Float)
    var isOpenNightTheme: Boolean
    val nightTheme: Int
    val nightThemeToString: String?
    var isUserCloseTheme: Boolean
    fun isActivityNeedRestart(oldTheme: Int): Boolean
    var isAutoOrManually: Boolean
    fun setManualEnableGeneral(enableGeneral: Boolean)
    fun setAutoEnableGeneral(enableGeneral: Boolean)
    val isEnableGeneral: Boolean
    val defaultShowPortfolioId: Int
    fun saveDefaultShowPortfolioId(portfolioId: Int)
    val environmentId: Int

    companion object {
        /***
         * 不通知配置改变
         */
        const val CODE_NO_NOTIFY_CHANGE = -1

        /**
         * 语言
         */
        const val CODE_LANGUAGE = 1

        /**
         * 主题
         */
        const val CODE_THEME = 2

        /**
         * 持仓盈利
         */
        const val CODE_HOLDING_SHOW = 3

        /**
         * 前台刷新平率
         */
        const val CODE_FOREGROUND_REFRESH_RATE = 4

        /**
         * 字体
         */
        const val CODE_FONT_SCHEME = 5

        /**
         * 涨跌幅颜色
         */
        const val CODE_COLOR_SCHEME = 6

        /**
         * 股票显示方式
         */
        const val CODE_NAME_SYMBOL = 7

        /**
         * 默认图标方式
         */
        const val CODE_DEFAULT_CHART = 8

        /**
         * 默认排序
         */
        const val CODE_ORDER_BY = 9

        /**
         * 组合列表样式
         */
        const val CODE_PORTFOLIO_LISTING = 10

        /**
         * 是否显示通知
         */
        const val CODE_NOTIFICATION_ENABLE = 11

        /**
         * 提醒策略
         */
        const val CODE_NOTIFICATION_PERIOD = 12

        /**
         * 谷歌同步
         */
        const val CODE_GOOGLE_FINANCE_SYNC = 13

        /**
         * 地区改变
         */
        const val CODE_MARKET_REGION = 14

        /**
         * 谷歌TOKEN刷新
         */
        const val CODE_GOOGLE_TOKEN_REFRESH = 15

        /**
         * 自动切换分时图
         */
        const val CODE_AUTO_SWITCH_DAILY_CHART = 16

        /**
         * 在浏览器中打开新闻
         */
        const val CODE_NEWS_OPEN_BROWSER = 17

        /**
         * 是否打开组合总览
         */
        const val CODE_ENABLE_GENERAL = 18
        const val FONT_SCHEME_SMALL = 0
        const val FONT_SCHEME_STANDARD = 1
        const val FONT_SCHEME_MEDIUM = 2
        const val FONT_SCHEME_LARGE = 3
        const val FONT_SCHEME_EX_LARGE = 4
        const val SETTINGS_SERVICE_NAME = "settings_service"
    }
}