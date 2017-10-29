package com.xiaomai.geek.data.module;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.UUID;

/**
 * Created by xiaomai on 2017/10/25.
 */

public class Password {

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
}
