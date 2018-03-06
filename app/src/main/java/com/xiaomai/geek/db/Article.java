package com.xiaomai.geek.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * Created by wangce on 2018/3/5.
 */

@Entity
public class Article implements Serializable {

    private static final long serialVersionUID = 536871008L;

    @Id(autoincrement = true)
    private Long id;
    private String name;    //给初学者的RxJava2.0教程(一)
    private String url;     //http://www.jianshu.com/p/464fa0252
    private String author;  //佚名
    private String keyword; //rxjava/初学者

    @Generated(hash = 760765743)
    public Article(Long id, String name, String url, String author,
                   String keyword) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.author = author;
        this.keyword = keyword;
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

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", author='" + author + '\'' +
                ", keyword='" + keyword + '\'' +
                "}\n";
    }
}