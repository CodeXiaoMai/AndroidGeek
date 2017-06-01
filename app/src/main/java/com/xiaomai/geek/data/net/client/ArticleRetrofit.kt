package com.xiaomai.geek.data.net.client

import okhttp3.OkHttpClient
import javax.inject.Inject

/**
 * Created by XiaoMai on 2017/6/1.
 */
class ArticleRetrofit @Inject constructor(var mCacheHttpClient: HttpClient) : BaseRetrofit() {

    val BASE_URL: String = "https://raw.githubusercontent.com/CodeXiaoMai/AndroidGeek/"

    override fun getBaseUrl(): String {
        return BASE_URL
    }

    override fun getHttpClient(): OkHttpClient {
        return mCacheHttpClient.get()
    }
}