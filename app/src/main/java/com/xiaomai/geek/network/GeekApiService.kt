package com.xiaomai.geek.network

import com.xiaomai.geek.common.ContentResponse
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Created by wangce on 2018/1/26.
 */
interface GeekApiService {

//    @Headers("Cache-Control: public, max-age=600")
    @GET("contents/app/src/main/assets/chapters?ref=master")
    fun getArticles(): Observable<ContentResponse>
}