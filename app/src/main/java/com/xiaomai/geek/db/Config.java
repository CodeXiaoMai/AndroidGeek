package com.xiaomai.geek.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wangce on 2018/2/1.
 */
@Entity
public class Config {

    @Id
    private Long id;

    private int version;

    private String notice;

    @Generated(hash = 561159903)
    public Config(Long id, int version, String notice) {
        this.id = id;
        this.version = version;
        this.notice = notice;
    }

    @Generated(hash = 589037648)
    public Config() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getNotice() {
        return this.notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

}
