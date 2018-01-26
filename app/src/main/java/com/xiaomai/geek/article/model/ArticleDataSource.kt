package com.xiaomai.geek.article.model

import rx.Observable

/**
 * Created by wangce on 2018/1/26.
 */
interface ArticleDataSource {

    fun getArticles() : Observable<List<ArticleResponse>>
}