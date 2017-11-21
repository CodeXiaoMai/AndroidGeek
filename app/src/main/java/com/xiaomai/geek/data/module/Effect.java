package com.xiaomai.geek.data.module;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.LayoutRes;

/**
 * Created by XiaoMai on 2017/11/21.
 */

public class Effect implements Parcelable{

    private String title;
    @LayoutRes
    private int layoutRes;

    public Effect(String title, @LayoutRes int layoutRes) {
        this.title = title;
        this.layoutRes = layoutRes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @LayoutRes
    public int getLayoutRes() {
        return layoutRes;
    }

    public void setLayoutRes(@LayoutRes int layoutRes) {
        this.layoutRes = layoutRes;
    }

    protected Effect(Parcel in) {
        this.title = in.readString();
        this.layoutRes = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeInt(this.layoutRes);
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
