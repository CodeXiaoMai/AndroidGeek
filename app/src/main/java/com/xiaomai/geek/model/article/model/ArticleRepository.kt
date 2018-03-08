package com.xiaomai.geek.model.article.model

import com.xiaomai.geek.BuildConfig
import com.xiaomai.geek.db.Article
import com.xiaomai.geek.db.ArticleRecord
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by wangce on 2018/1/26.
 *
 * 先从 Server 获取数据，再保存到本地数据库，然后根据本地文章阅读情况展示数据
 */
class ArticleRepository(private val articleLocalDataSource: ArticleLocalDataSource,
                        private val articleRemoteDataSource: ArticleRemoteDataSource
) : ArticleDataSource {

    override fun getArticleResponse(): Observable<ArticleResponse> {
        return if (BuildConfig.DEBUG) {
            articleLocalDataSource.getArticleResponse()
        } else {
            articleRemoteDataSource.getArticleResponse()
        }
    }

    override fun saveArticles(articleResponse: ArticleResponse): Completable {
        return articleLocalDataSource.saveArticles(articleResponse)
    }

    override fun saveArticleRecord(articleRecord: ArticleRecord): Observable<Boolean> {
        return articleLocalDataSource.saveArticleRecord(articleRecord)
    }

    override fun loadArticleRecord(article: Article): Observable<ArticleRecord> {
        return articleLocalDataSource.loadArticleRecord(article)
    }

    override fun searchArticle(keyword: String): Single<List<Article>> =
            articleLocalDataSource.searchArticle(keyword)
}