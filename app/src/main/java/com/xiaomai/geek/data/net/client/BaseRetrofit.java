package com.xiaomai.geek.data.net.client;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by XiaoMai on 2017/4/24.
 */

public abstract class BaseRetrofit {

    public Retrofit get() {
        Retrofit.Builder builder = new Retrofit.Builder();

        builder.baseUrl(getBaseUrl())
                .client(getHttpClient())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());

        return builder.build();
    }

    public abstract String getBaseUrl();

    public abstract OkHttpClient getHttpClient();
}
