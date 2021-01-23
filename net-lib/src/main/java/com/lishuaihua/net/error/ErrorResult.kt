package com.lishuaihua.net.error

class ErrorResult {
    var code: Int = 0
    var msg: String? = ""

    constructor()

    constructor(code: Int, msg: String? = "") {
        this.code = code
        this.msg = msg
    }
}