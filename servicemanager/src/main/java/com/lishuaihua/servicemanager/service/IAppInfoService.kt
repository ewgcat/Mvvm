package com.lishuaihua.servicemanager.service

interface IAppInfoService {
    val applicationName: String?
    val applciationVersionName: String?
    val applicationVersionCode: String?
    val applicationDebug: Boolean

    companion object {
        const val APP_INFO_SERVICE_NAME = "app_info_service"
    }
}