package com.xiaomai.geek.data.module;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by XiaoMai on 2017/11/21.
 */

public class Effect implements Parcelable {

    @NonNull
    private String title;
    @LayoutRes
    private int layoutRes;
    @Nullable
    private String clazzName;

    public Effect(@NonNull String title, @LayoutRes int layoutRes) {
        this.title = title;
        this.layoutRes = layoutRes;
    }

    public Effect(@NonNull String title, String clazzName) {
        this.title = title;
        this.clazzName = clazzName;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @LayoutRes
    public int getLayoutRes() {
        return layoutRes;
    }

    public void setLayoutRes(@LayoutRes int layoutRes) {
        this.layoutRes = layoutRes;
    }

    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

    protected Effect(Parcel in) {
        this.title = in.readString();
        this.layoutRes = in.readInt();
        this.clazzName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeInt(this.layoutRes);
        dest.writeString(this.clazzName);
    }

    public static final Creator<Effect> CREATOR = new Creator<Effect>() {
        @Override
        public Effect createFromParcel(Parcel source) {
            return new Effect(source);
        }

        @Override
        public Effect[] newArray(int size) {
            return new Effect[size];
        }
    };
}
