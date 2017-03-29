
package com.xiaomai.geek.api.client;

import javax.inject.Inject;

import okhttp3.OkHttpClient;

/**
 * Created by XiaoMai on 2017/3/27 10:31.
 */

public class NewsRetrofit extends BaseRetrofit {

    private static final String NEWS_HOST = "http://c.3g.163.com/";

    GeekHttpClient mHttpClient;

    @Inject
    public NewsRetrofit(GeekHttpClient mHttpClient) {
        this.mHttpClient = mHttpClient;
    }

    @Override
    protected ApiEndPoint getApiEndPoint() {
        return new ApiEndPoint() {
            @Override
            public String getEndPoint() {
                return NEWS_HOST;
            }
        };
    }

    @Override
    protected OkHttpClient getHttpClient() {
        return mHttpClient.get();
    }
}
