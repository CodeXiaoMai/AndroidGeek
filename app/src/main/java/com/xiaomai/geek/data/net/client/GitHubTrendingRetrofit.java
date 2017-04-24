package com.xiaomai.geek.data.net.client;

import javax.inject.Inject;

import okhttp3.OkHttpClient;

/**
 * Created by XiaoMai on 2017/4/24.
 */

public class GitHubTrendingRetrofit extends BaseRetrofit {

    public static final String BASE_URL = "https://api.github.com/";

    HttpClient mCacheHttpClient;

    @Inject
    public GitHubTrendingRetrofit(HttpClient httpClient) {
        mCacheHttpClient = httpClient;
    }

    @Override
    public String getBaseUrl() {
        return BASE_URL;
    }

    @Override
    public OkHttpClient getHttpClient() {
        return mCacheHttpClient.get();
    }
}
