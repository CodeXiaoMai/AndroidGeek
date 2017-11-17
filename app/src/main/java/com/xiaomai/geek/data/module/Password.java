package com.xiaomai.geek.data.module;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.UUID;

/**
 * Created by xiaomai on 2017/10/25.
 */

public class Password implements Parcelable{

    @NonNull
    private String id;

    @Nullable
    private String platform;

    @Nullable
    private String userName;

    @Nullable
    private String password;

    @Nullable
    private String note;

    public Password(String id, String platform, String userName, String password, String note) {
        this.id = id;
        this.platform = platform;
        this.userName = userName;
        this.password = password;
        this.note = note;
    }

    public Password(String platform, String userName, String password, String note) {
        this(UUID.randomUUID().toString(), platform, userName, password, note);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(platform)
                && TextUtils.isEmpty(userName)
                && TextUtils.isEmpty(password)
                && TextUtils.isEmpty(note);
    }

    @Override
    public String toString() {
        return "Password{" +
                "id='" + id + '\'' +
                ", platform='" + platform + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", note='" + note + '\'' +
                '}';
    }

    protected Password(Parcel in) {
        this.id = in.readString();
        this.platform = in.readString();
        this.userName = in.readString();
        this.password = in.readString();
        this.note = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.platform);
        dest.writeString(this.userName);
        dest.writeString(this.password);
        dest.writeString(this.note);
    }

    public static final Creator<Password> CREATOR = new Creator<Password>() {
        @Override
        public Password createFromParcel(Parcel source) {
            return new Password(source);
        }

        @Override
        public Password[] newArray(int size) {
            return new Password[size];
        }
    };
}
