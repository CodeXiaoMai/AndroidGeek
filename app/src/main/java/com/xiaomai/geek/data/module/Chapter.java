package com.xiaomai.geek.data.module;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by XiaoMai on 2017/5/16.
 */

public class Chapter implements Parcelable {

    private String name;

    private String description;

    private String image;

    private String url;

    private String owner;

    private String repoName;

    private List<Article> articles;

    public Chapter() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeString(this.image);
        dest.writeString(this.url);
        dest.writeString(this.owner);
        dest.writeString(this.repoName);
        dest.writeTypedList(this.articles);
    }

    protected Chapter(Parcel in) {
        this.name = in.readString();
        this.description = in.readString();
        this.image = in.readString();
        this.url = in.readString();
        this.owner = in.readString();
        this.repoName = in.readString();
        this.articles = in.createTypedArrayList(Article.CREATOR);
    }

    public static final Creator<Chapter> CREATOR = new Creator<Chapter>() {
        @Override
        public Chapter createFromParcel(Parcel source) {
            return new Chapter(source);
        }

        @Override
        public Chapter[] newArray(int size) {
            return new Chapter[size];
        }
    };
}
