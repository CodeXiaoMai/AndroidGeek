package com.xiaomai.geek.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wangce on 2018/3/5.
 */

@Entity
public class Config {

    @Id(autoincrement = true)
    private Long id;

    private long version;

    private String notice;

    @Generated(hash = 849651376)
    public Config(Long id, long version, String notice) {
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

    public long getVersion() {
        return this.version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getNotice() {
        return this.notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }
}