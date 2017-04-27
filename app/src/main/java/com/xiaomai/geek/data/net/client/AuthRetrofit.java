package com.xiaomai.geek.data.net.client;

import javax.inject.Inject;

import okhttp3.OkHttpClient;

/**
 * Created by XiaoMai on 2017/4/26.
 */

public class AuthRetrofit extends BaseRetrofit {

    public static final String BASE_URL = "https://api.github.com/";

    private String userName;

    private String password;

    @Inject
    public AuthRetrofit() {
    }

    @Override
    public String getBaseUrl() {
        return BASE_URL;
    }

    public void setAuthInfo(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    @Override
    public OkHttpClient getHttpClient() {
        return new AuthHttpClient(userName, password).get();
    }
}
