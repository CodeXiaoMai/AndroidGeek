package com.xiaomai.geek.data.net.client;

import android.text.TextUtils;
import android.util.Base64;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by XiaoMai on 2017/4/26.
 */

public class AuthHttpClient extends BaseOkHttpClient {

    private String userName;

    private String password;

    public AuthHttpClient(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    @Override
    public OkHttpClient.Builder customize(OkHttpClient.Builder builder) {
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    // https://developer.github.com/v3/auth/#basic-authentication
                    // https://developer.github.com/v3/oauth/#non-web-application-flow
                    String userCredentials = userName + ":" + password;
                    String basicAuth = "Basic " + new String(
                            Base64.encode(userCredentials.getBytes(), Base64.DEFAULT));

                    Request original = chain.request();

                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", basicAuth.trim());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        }
        return builder;
    }
}
