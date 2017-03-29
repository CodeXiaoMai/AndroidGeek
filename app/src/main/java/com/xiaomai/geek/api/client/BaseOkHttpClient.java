
package com.xiaomai.geek.api.client;

import android.app.Application;

import com.xiaomai.geek.common.util.NetworkUtil;
import com.xiaomai.geek.common.wrapper.AppLog;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by XiaoMai on 2017/3/24 18:50.
 */

public abstract class BaseOkHttpClient {

    @Inject
    Application mContext;

    // 缓存有效期为 1 天
    private static final long CACHE_STALE_SEC = 60 * 60 * 24;

    // 查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
    private static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale="
            + CACHE_STALE_SEC;


    private static final String WELFARE_HOST = "http://gank.io/";


    public OkHttpClient get() {
        Cache cache = new Cache(new File(mContext.getCacheDir(), "HttpCache"), 100 * 1024 * 1024);
        OkHttpClient.Builder builder = new OkHttpClient.Builder().cache(cache)
                .retryOnConnectionFailure(true).addInterceptor(mLoggerInterceptor)
                .addInterceptor(mReWriteCacheControlInterceptor)
                .addNetworkInterceptor(mReWriteCacheControlInterceptor)
                .connectTimeout(10, TimeUnit.SECONDS);
        builder = customize(builder);
        return builder.build();
    }

    protected abstract OkHttpClient.Builder customize(OkHttpClient.Builder builder);

    private final Interceptor mLoggerInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            final Request request = chain.request();
            Buffer requestBuffer = new Buffer();
            if (request.body() != null)
                request.body().writeTo(requestBuffer);
            else
                AppLog.d("request.body() == null");
            // 打印Url信息
            AppLog.w(request.url() + (request.body() != null
                    ? "?" + parseParams(request.body(), requestBuffer) : ""));
            return chain.proceed(request);
        }
    };

    /**
     * 解析Url中的请求参数
     * 
     * @param body
     * @param requestBuffer
     * @return
     * @throws UnsupportedEncodingException
     */
    private String parseParams(RequestBody body, Buffer requestBuffer)
            throws UnsupportedEncodingException {
        if (body.contentType() != null && !body.contentType().toString().contains("multipart"))
            return URLDecoder.decode(requestBuffer.readUtf8(), "UTF-8");
        return "";
    }

    /**
     * 响应头拦截器，用来配置缓存策略
     */
    private final Interceptor mReWriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetworkUtil.isNetworkAvailable(mContext)) {
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
                AppLog.e("No netWork！！！");
            }
            Response originalResponse = chain.proceed(request);
            if (NetworkUtil.isNetworkAvailable(mContext)) {
                // 读取接口上的@Headers的配置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder().header("Cache-Control", cacheControl)
                        .removeHeader("Pragma").build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, " + CACHE_CONTROL_CACHE)
                        .removeHeader("Pragma").build();
            }
        }
    };
}
