package com.lishuaihua.data_module.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class KeyWordBean {

    @Id(autoincrement = true)
    Long id;
    String keyword;
    @Generated(hash = 1121882475)
    public KeyWordBean(Long id, String keyword) {
        this.id = id;
        this.keyword = keyword;
    }
    @Generated(hash = 673900408)
    public KeyWordBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getKeyword() {
        return this.keyword;
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}

