package com.xiaomai.geek.model.article.model

import com.xiaomai.geek.db.Article
import com.xiaomai.geek.db.ArticleRecord
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by wangce on 2018/1/26.
 */
interface ArticleDataSource {

    fun getArticleResponse(): Observable<ArticleResponse>

    fun saveArticles(articleResponse: ArticleResponse): Completable

    fun saveArticleRecord(articleRecord: ArticleRecord): Observable<Boolean>

    fun loadArticleRecord(article: Article): Observable<ArticleRecord>

    fun searchArticle(keyword: String): Single<List<Article>>
}