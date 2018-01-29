package com.xiaomai.geek.article.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xiaomai.geek.common.utils.StringUtil
import com.xiaomai.geek.common.wrapper.GeeKLog
import com.xiaomai.geek.network.GeekApiService
import com.xiaomai.geek.network.GeekRetrofit
import io.reactivex.Observable

/**
 * Created by wangce on 2018/1/26.
 */
class ArticleRemoteDataSource : ArticleDataSource {

    override fun getArticles(): Observable<List<ArticleResponse>> {
        return GeekRetrofit.getInstance().create(GeekApiService::class.java)
                .getArticles()
                .map {
                    it.content?.let {
                        val collectionType = object : TypeToken<List<ArticleResponse>>() {
                        }.type
                        val json = StringUtil.base64Decode(it)
                        GeeKLog.json(json)
                        Gson().fromJson<List<ArticleResponse>>(json, collectionType)
                    }
                }
    }
}