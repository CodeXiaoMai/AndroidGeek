package com.xiaomai.geek.data.net.client;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by XiaoMai on 2017/4/24.
 */

public abstract class BaseOkHttpClient {

    private static final long TIME_OUT = 30 * 1000;

    public OkHttpClient get() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS);
        builder.addNetworkInterceptor(new StethoInterceptor());
        builder = customize(builder);
        return builder.build();
    }

    public abstract OkHttpClient.Builder customize(OkHttpClient.Builder builder);
}
