package com.lishuaihua.data_module.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
class UserInfo {

    /**
     * id : 1
     * nickname : 张三
     * sex : 30
     */

    @Id(assignable = true)
    var id: Long = 1
    var nickname: String? = null
    var sex: Int = 0
    constructor(
        id: Long,
        nickname: String?,
        sex: Int,
    ) {
        this.id = id
        this.nickname = nickname
        this.sex = sex
    }

    constructor()

}



