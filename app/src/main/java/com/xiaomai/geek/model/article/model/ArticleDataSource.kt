package com.xiaomai.geek.model.article.model

import com.xiaomai.geek.db.ArticleRecord
import io.reactivex.Observable

/**
 * Created by wangce on 2018/1/26.
 */
interface ArticleDataSource {

    fun getArticleResponse(): Observable<ArticleResponse>

    fun saveArticleRecord(articleRecord: ArticleRecord): Observable<Boolean>

    fun loadArticleRecord(article: Article): Observable<ArticleRecord>
}