package com.xiaomai.geek.data.net

import com.xiaomai.geek.data.module.Video
import retrofit2.http.GET
import retrofit2.http.Headers
import rx.Observable

/**
 * Created by XiaoMai on 2017/6/23.
 */
interface VideoService {

    @Headers("Cache-Control: public, max-age=600")
    @GET("contents/app/src/main/assets/videos?ref=master")
    fun getVideos() : Observable<List<Video>>
}