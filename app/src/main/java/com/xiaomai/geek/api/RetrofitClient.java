
package com.xiaomai.geek.api;

import android.app.Application;

import java.io.File;

import javax.inject.Inject;

import okhttp3.Cache;

/**
 * Created by XiaoMai on 2017/3/24 18:50.
 */

public class RetrofitClient {

    @Inject
    Application mContext;

    public void init() {
        Cache cache = new Cache(new File(mContext.getCacheDir(), "HttpCache"), 100 * 1024 * 1024);
    }

}
