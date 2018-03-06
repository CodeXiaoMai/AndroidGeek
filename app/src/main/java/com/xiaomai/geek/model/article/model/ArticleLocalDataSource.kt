package com.xiaomai.geek.model.article.model

import android.app.Application
import com.xiaomai.geek.application.GeekApplication
import com.xiaomai.geek.common.utils.AssetUtil
import com.xiaomai.geek.db.Article
import com.xiaomai.geek.db.ArticleDao
import com.xiaomai.geek.db.ArticleRecord
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by wangce on 2018/1/26.
 */
class ArticleLocalDataSource(private val context: Application
) : ArticleDataSource {
    private val TAG = this::class.java.simpleName
    private val FILE_NAME = "articles"

    override fun getArticleResponse(): Observable<ArticleResponse> {
        return Observable.create {
            val articleResponse = AssetUtil.readFromAsset(context, FILE_NAME, ArticleResponse::class.java)
            if (articleResponse != null) {
                it.onNext(articleResponse)
                it.onComplete()
            } else {
                it.onError(Throwable())
            }
        }
    }

    override fun saveArticles(articleResponse: ArticleResponse): Completable {
        return Completable.fromAction {
            var localVersion = 0L
            GeekApplication.DAO_SESSION.configDao.apply {
                val loadAll = loadAll()
                if (loadAll.isNotEmpty()) {
                    localVersion = loadAll[0].version
                }
            }
            if (localVersion < articleResponse.version) {
                deleteArticles()
                saveArticleResponse(articleResponse)
            } else {
                throw Throwable("数据库中已经是最新记录")
            }
        }
    }

    private fun deleteArticles() {
        GeekApplication.DAO_SESSION.articleDao.deleteAll()
    }

    private fun saveArticleResponse(articleResponse: ArticleResponse) {
        val articles = mutableListOf<Article>()
        articleResponse.category.forEach {
            articles.addAll(it.articles)
        }

        GeekApplication.DAO_SESSION.articleDao.saveInTx(articles)
    }

    override fun saveArticleRecord(articleRecord: ArticleRecord): Observable<Boolean> {
        return Observable.create {
            GeekApplication.DAO_SESSION.articleRecordDao.apply {
                val resultList = queryRaw("where url = ?", articleRecord.url)
                if (resultList.isEmpty()) {
                    // 如果数据库中没有保存此 url，插入数据库
                    articleRecord.times = 1
                    val insert = insert(articleRecord)
                    it.onNext(insert > 0)
                    it.onComplete()
                } else {
                    // 如果数据库中有此 url，更新数据库
                    resultList[0].apply {
                        url = articleRecord.url
                        name = articleRecord.name
                        author = articleRecord.author
                        keywords = articleRecord.keywords
                        progress = articleRecord.progress
                        readTime = articleRecord.readTime
                        times++
                        update(this)
                        it.onNext(true)
                        it.onComplete()
                    }
                }
            }
        }
    }

    override fun loadArticleRecord(article: Article): Observable<ArticleRecord> {
        return Observable.create {
            GeekApplication.DAO_SESSION.articleRecordDao.apply {
                val list = queryRaw("where url = ?", article.url)
                if (list.isNotEmpty()) {
                    it.onNext(list[0])
                    it.onComplete()
                } else {
                    it.onError(Throwable("no record"))
                }
            }
        }
    }

    override fun searchArticle(keyword: String): Single<List<Article>> {
        return Single.create {
            GeekApplication.DAO_SESSION.articleDao.apply {
                val nameLike = ArticleDao.Properties.Name.like("%$keyword%")
                val authorLike = ArticleDao.Properties.Author.like("%$keyword%")
                val keywordLike = ArticleDao.Properties.Keyword.like("%$keyword%")

                val list = queryBuilder().whereOr(nameLike, authorLike, keywordLike).build()
                        .list().toList()
                if (list.isNotEmpty()) {
                    it.onSuccess(list)
                } else {
                    it.onError(Throwable("没有搜索到相关内容"))
                }
            }
        }
    }
}