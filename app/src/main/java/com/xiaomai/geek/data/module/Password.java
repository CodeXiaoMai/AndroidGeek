
package com.xiaomai.geek.data.module;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by XiaoMai on 2017/3/30 10:06.
 */

public class Password implements Parcelable, Comparable<Password> {

    private int id;

    private String platform;

    private String userName;

    private String password;

    private String category;

    private String note;

    private boolean star;

    private long time;

    public Password() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isStar() {
        return star;
    }

    public void setStar(boolean star) {
        this.star = star;
    }

    public void setStar(int star) {
        this.star = star > 0;
    }

    public long getTime() {
        return time;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.platform);
        dest.writeString(this.userName);
        dest.writeString(this.password);
        dest.writeString(this.category);
        dest.writeString(this.note);
        dest.writeByte(this.star ? (byte) 1 : (byte) 0);
        dest.writeLong(this.time);
    }

    protected Password(Parcel in) {
        this.id = in.readInt();
        this.platform = in.readString();
        this.userName = in.readString();
        this.password = in.readString();
        this.category = in.readString();
        this.note = in.readString();
        this.star = in.readByte() != 0;
        this.time = in.readLong();
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

    @Override
    public String toString() {
        return "Password{" + "id='" + id + '\'' + ", platform='" + platform + '\'' + ", userName='"
                + userName + '\'' + ", password='" + password + '\'' + ", category='" + category
                + '\'' + ", note='" + note + '\'' + ", star=" + star + ", time=" + time + '}';
    }

    @Override
    public int compareTo(@NonNull Password o) {
        return o.getPlatform().compareToIgnoreCase(getPlatform());
    }
}
