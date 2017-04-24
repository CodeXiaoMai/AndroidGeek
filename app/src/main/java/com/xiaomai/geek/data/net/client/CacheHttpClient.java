package com.xiaomai.geek.data.net.client;

import android.app.Application;

import com.xiaomai.geek.common.utils.NetworkUtil;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by XiaoMai on 2017/4/24.
 */

public class CacheHttpClient extends BaseOkHttpClient {

    private static final long CACHE_SIZE = 1024 * 1024 * 100;

    @Inject
    Application mContext;

    @Inject
    public CacheHttpClient() {
    }

    @Override
    public OkHttpClient.Builder customize(OkHttpClient.Builder builder) {
        File cacheFile = new File(mContext.getCacheDir(), "git_hub");
        Cache cache = new Cache(cacheFile, CACHE_SIZE);
        builder.cache(cache);

        builder.addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                // Add FORCE_CACHE cache control for each request if network is not available.
                if (!NetworkUtil.isNetworkAvailable(mContext)) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response originalResponse = chain.proceed(request);
                if (NetworkUtil.isNetworkAvailable(mContext)) {
                    String cacheControl = request.cacheControl().toString();
                    // Add cache control header for response same as request's while network is available.
                    return originalResponse.newBuilder()
                            .header("Cache-Control", cacheControl)
                            .build();
                } else {
                    // Add cache control header for response to FORCE_CACHE while network is not available.
                    return originalResponse.newBuilder()
                            .header("Cache-Control", CacheControl.FORCE_CACHE.toString())
                            .build();
                }
            }
        });
        return builder;
    }
}
