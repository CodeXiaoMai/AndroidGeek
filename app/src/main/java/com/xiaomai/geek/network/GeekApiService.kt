package com.xiaomai.geek.network

import com.xiaomai.geek.common.ContentResponse
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Created by wangce on 2018/1/26.
 */
interface GeekApiService {

    @GET("contents/app/src/main/assets/articles?ref=master")
    fun getArticleResponse(): Observable<ContentResponse>
}