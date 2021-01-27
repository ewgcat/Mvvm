package com.lishuaihua.servicemanager.service

/**
 * 描述:  与设备相关的配置信息
 */
interface IDeviceService {
    var model: String?

    /***
     * 获取MCC
     */
    var mcc: String?

    /***
     * 获取Operator MCC
     */
    var networkMcc: String?

    /***
     * 获取Mnc
     */
    var mnc: String?

    /***
     * 获取Operator MCC
     */
    var networkMnc: String?

    /**
     * 获取设备默认语言
     */
    var deviceDefaultLanguage: String?

    /**
     * 获取ClientId
     *
     * @return
     */
    var clientId: String?

    /**
     * 获取老版本ClientId
     *
     * @return
     */
    var oldVersionCode: Int

    /**
     * 获取当前设备安卓系统版本号
     */
    var systemVersion: String?

    /**
     * 获取手机品牌
     *
     * @return
     */
    var phoneBrand: String?

    /**
     * 获取手机Android API等级（22、23 ...）
     *
     * @return
     */
    var buildLevel: Int

    /**
     * 获取设备宽度（px）
     *
     * @return
     */
    var deviceWidth: Int

    /**
     * 获取设备高度（px）
     *
     * @return
     */
    var deviceHeight: Int

    /**
     * SD卡判断
     *
     * @return
     */
    var isSDCardAvailable: Boolean

    /**
     * 是否有网
     *
     * @return
     */
    var isNetworkConnected: Boolean

    /**
     * 是否这次安装时新安装
     */
    var isNewInstall: Boolean

    /**
     * 是否是三星设备
     * @return
     */
    var isSamSungDevice: Boolean

    companion object {
        const val DEVICE_SERVICE_NAME = "device_service"
    }
}