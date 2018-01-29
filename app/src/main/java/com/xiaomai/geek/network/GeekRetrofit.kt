package com.xiaomai.geek.network

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by wangce on 2018/1/26.
 */
object GeekRetrofit {

    private const val BASE_URL = "https://api.github.com/repos/CodeXiaoMai/AndroidGeek/"

    fun getInstance(): Retrofit {
        val builder: Retrofit.Builder = Retrofit.Builder()
        builder.baseUrl(BASE_URL)
                .client(OkHttpClient.Builder().apply {
                    connectTimeout(30 * 1000, TimeUnit.MILLISECONDS)
                    addInterceptor {
                        val request = it.request()
                        val build = request.newBuilder()
                                .header("Accept", "application/vnd.github.v3.json")
                                .header("User-Agent", "GitHub").build()
                        it.proceed(build)
                    }
                    addNetworkInterceptor(StethoInterceptor())
                }.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
        return builder.build()
    }
}