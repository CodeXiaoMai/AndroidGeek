package com.xiaomai.geek.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wangce on 2018/1/30.
 */

@Entity
public class ArticleRecord {

    @Id(autoincrement = true)
    private Long id;

    @Unique
    private String url;

    private String name;

    // 作者
    private String author;

    // 关键词
    private String keywords;

    // 阅读时间
    private long readTime;

    // 阅读次数
    private int times;

    // 阅读进度
    private float progress;

    @Generated(hash = 1797519757)
    public ArticleRecord(Long id, String url, String name, String author,
            String keywords, long readTime, int times, float progress) {
        this.id = id;
        this.url = url;
        this.name = name;
        this.author = author;
        this.keywords = keywords;
        this.readTime = readTime;
        this.times = times;
        this.progress = progress;
    }

    @Generated(hash = 1706100913)
    public ArticleRecord() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getKeywords() {
        return this.keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public long getReadTime() {
        return this.readTime;
    }

    public void setReadTime(long readTime) {
        this.readTime = readTime;
    }

    public int getTimes() {
        return this.times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public float getProgress() {
        return this.progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }
}
