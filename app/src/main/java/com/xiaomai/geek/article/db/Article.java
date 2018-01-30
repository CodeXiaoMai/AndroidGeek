package com.xiaomai.geek.article.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wangce on 2018/1/30.
 */

@Entity
public class Article {

    @Id(autoincrement = true)
    private Long id;

    private String name;

    private String url;

    @Generated(hash = 1196083788)
    public Article(Long id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    @Generated(hash = 742516792)
    public Article() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
