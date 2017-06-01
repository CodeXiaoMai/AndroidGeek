package com.xiaomai.geek.data.net.response

import com.xiaomai.geek.data.module.Chapter
import retrofit2.http.GET
import retrofit2.http.Headers
import rx.Observable

/**
 * Created by XiaoMai on 2017/5/31.
 */
interface ArticleService {

    @Headers("Cache-Control: public, max-age=600")
    @GET("master/app/src/main/assets/chapters")
    fun getChapters(): Observable<List<Chapter>>
}