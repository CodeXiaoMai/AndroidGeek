package com.xiaomai.geek.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by wangce on 2018/1/30.
 */

@Entity
public class Article {

    @Id(autoincrement = true)
    private Long id;

    private String categoryId;

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

    @Generated(hash = 1428906368)
    public Article(Long id, String categoryId, String url, String name,
            String author, String keywords, long readTime, int times,
            float progress) {
        this.id = id;
        this.categoryId = categoryId;
        this.url = url;
        this.name = name;
        this.author = author;
        this.keywords = keywords;
        this.readTime = readTime;
        this.times = times;
        this.progress = progress;
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

    public String getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Article article = (Article) o;

        if (categoryId != null ? !categoryId.equals(article.categoryId) : article.categoryId != null)
            return false;
        if (url != null ? !url.equals(article.url) : article.url != null) return false;
        if (name != null ? !name.equals(article.name) : article.name != null) return false;
        if (author != null ? !author.equals(article.author) : article.author != null) return false;
        return keywords != null ? keywords.equals(article.keywords) : article.keywords == null;
    }

    @Override
    public int hashCode() {
        int result = categoryId != null ? categoryId.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (keywords != null ? keywords.hashCode() : 0);
        return result;
    }
}
