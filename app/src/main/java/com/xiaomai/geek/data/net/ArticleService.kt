package com.xiaomai.geek.data.net

import com.xiaomai.geek.data.net.response.ChapterResp
import retrofit2.http.GET
import retrofit2.http.Headers
import rx.Observable

/**
 * Created by XiaoMai on 2017/5/31.
 */
interface ArticleService {

    @Headers("Cache-Control: public, max-age=600")
    @GET("contents/app/src/main/assets/chapters?ref=master")
    fun getChapters(): Observable<ChapterResp>
}