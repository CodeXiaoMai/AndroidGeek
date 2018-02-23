package com.xiaomai.geek.article.model

import com.google.gson.Gson
import com.xiaomai.geek.common.utils.StringUtil
import com.xiaomai.geek.common.wrapper.GeeKLog
import com.xiaomai.geek.network.GeekApiService
import com.xiaomai.geek.network.GeekRetrofit
import io.reactivex.Observable

/**
 * Created by wangce on 2018/1/26.
 */
class ArticleRemoteDataSource {

    fun getArticleResponse(): Observable<ArticleResponse> {
        return GeekRetrofit.getInstance().create(GeekApiService::class.java)
                .getArticleResponse()
                .map { contentResponse ->
                    contentResponse.content?.let {
                        val json = StringUtil.base64Decode(it)
                        GeeKLog.json(json)
                        Gson().fromJson<ArticleResponse>(json, ArticleResponse::class.java)
                    }
                }
    }
}