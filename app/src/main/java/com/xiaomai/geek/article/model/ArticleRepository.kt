package com.xiaomai.geek.article.model

import rx.Observable


/**
 * Created by wangce on 2018/1/26.
 */
class ArticleRepository(private val remoteDataSource: ArticleRemoteDataSource) : ArticleDataSource {

    override fun getArticles(): Observable<List<ArticleResponse>> {
        return remoteDataSource.getArticles()
    }
}