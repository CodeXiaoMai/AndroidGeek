
package com.xiaomai.geek.api.client;

import android.app.Application;

import javax.inject.Inject;

import okhttp3.OkHttpClient;

/**
 * Created by XiaoMai on 2017/3/27 10:41.
 */

public class GeekHttpClient extends BaseOkHttpClient {

    @Inject
    public Application application;

    @Inject
    public GeekHttpClient() {
    }

    @Override
    protected OkHttpClient.Builder customize(OkHttpClient.Builder builder) {
        return builder;
    }

}
