package com.xiaomai.geek.model.article.viewmodel

import android.app.Application
import com.xiaomai.geek.base.BaseViewModel
import com.xiaomai.geek.base.observer.BaseCompletableObserver
import com.xiaomai.geek.base.observer.BaseObserver
import com.xiaomai.geek.base.observer.BaseSingleObserver
import com.xiaomai.geek.common.PageStatus
import com.xiaomai.geek.common.wrapper.GeeKLog
import com.xiaomai.geek.db.Article
import com.xiaomai.geek.db.ArticleRecord
import com.xiaomai.geek.db.Config
import com.xiaomai.geek.model.article.model.ArticleLocalDataSource
import com.xiaomai.geek.model.article.model.ArticleRemoteDataSource
import com.xiaomai.geek.model.article.model.ArticleRepository
import com.xiaomai.geek.model.article.model.ArticleResponse
import com.xiaomai.geek.model.main.model.ConfigRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by wangce on 2018/1/29.
 */
class ArticleViewModel(context: Application) : BaseViewModel(context) {

    private val articleRepository: ArticleRepository =
            ArticleRepository(ArticleLocalDataSource(getApplication()), ArticleRemoteDataSource())

    private val configRepository = ConfigRepository(getApplication())

    fun loadArticles(): Observable<ArticleResponse> {
        return articleRepository.getArticleResponse()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { value ->
                    pageStatus.value = if (value.category.isEmpty()) PageStatus.EMPTY else PageStatus.NORMAL
                }
    }

    fun saveArticlesAndConfig(articleResponse: ArticleResponse) {
        articleRepository.saveArticles(articleResponse)
                .subscribeOn(Schedulers.io())
                .subscribe(object : BaseCompletableObserver() {
                    override fun onComplete() {
                        GeeKLog.d(TAG, "articles成功保存到数据库")
                        saveConfig(articleResponse)
                    }
                })
    }

    private fun saveConfig(articleResponse: ArticleResponse) {
        configRepository.saveConfig(Config().apply {
            version = articleResponse.version
        }).subscribeOn(Schedulers.io())
                .subscribe(object : BaseCompletableObserver() {
                    override fun onComplete() {
                        GeeKLog.d(TAG, "config成功保存到数据库")
                    }
                })
    }

    fun saveArticleRecord(article: Article, readTime: Long, progress: Float) {
        if (progress <= 0) {
            return
        }
        val articleRecord = ArticleRecord()
        articleRecord.url = article.url
        articleRecord.keywords = article.keyword
        articleRecord.name = article.name
        articleRecord.progress = progress
        articleRecord.readTime = readTime
        articleRecord.author = article.author
        articleRepository.saveArticleRecord(articleRecord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseObserver<Boolean>() {
                    override fun onSuccess(value: Boolean) {
                        GeeKLog.d(TAG, "阅读记录成功保存到数据库")
                    }
                })
    }

    fun loadArticleRecord(article: Article): Observable<ArticleRecord> {
        return articleRepository.loadArticleRecord(article)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    if (it.progress > 0) {
                        showSnackBar("已自动跳转到上次阅读的位置")
                    }
                }
    }

    fun searchArticle(keyword: String) {
        articleRepository.searchArticle(keyword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseSingleObserver<List<Article>>() {
                    override fun onSuccess(t: List<Article>) {
                        GeeKLog.d(msg = "搜索结果：$t")
                    }
                })
    }
}