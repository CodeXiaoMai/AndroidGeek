package com.xiaomai.geek.data.module

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by XiaoMai on 2017/6/23.
 */
public class Video : Parcelable {

    var name: String? = null
    var url: String? = null

    constructor() {}

    constructor(name: String, url: String) {
        this.name = name
        this.url = url
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    protected constructor(`in`: Parcel) {
        this.name = `in`.readString()
        this.url = `in`.readString()
    }

    companion object {

        @JvmField val CREATOR: Parcelable.Creator<Video> = object : Parcelable.Creator<Video> {
            override fun createFromParcel(source: Parcel): Video {
                return Video(source)
            }

            override fun newArray(size: Int): Array<Video?> {
                return arrayOfNulls(size)
            }
        }
    }
}