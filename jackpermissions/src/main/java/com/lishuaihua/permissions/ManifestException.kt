package com.lishuaihua.permissions

internal class ManifestException : RuntimeException {
    constructor() : super("No permissions are registered in the manifest file") {
        // 清单文件中没有注册任何权限
    }

    constructor(permission: String) : super("$permission: Permissions are not registered in the manifest file") {
        // 申请的危险权限没有在清单文件中注册
    }
}