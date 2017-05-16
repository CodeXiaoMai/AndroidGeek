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

    private List<Article> articles;

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
        dest.writeTypedList(this.articles);
    }

    public Chapter() {
    }

    public Chapter(String name, String description, String image, String url, List<Article> articles) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.url = url;
        this.articles = articles;
    }

    protected Chapter(Parcel in) {
        this.name = in.readString();
        this.description = in.readString();
        this.image = in.readString();
        this.url = in.readString();
        this.articles = in.createTypedArrayList(Article.CREATOR);
    }

    public static final Parcelable.Creator<Chapter> CREATOR = new Parcelable.Creator<Chapter>() {
        @Override
        public Chapter createFromParcel(Parcel source) {
            return new Chapter(source);
        }

        @Override
        public Chapter[] newArray(int size) {
            return new Chapter[size];
        }
    };

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

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
