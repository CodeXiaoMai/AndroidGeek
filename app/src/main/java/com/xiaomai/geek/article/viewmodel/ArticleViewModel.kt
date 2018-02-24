package com.xiaomai.geek.article.viewmodel

import android.app.Application
import com.xiaomai.geek.article.model.*
import com.xiaomai.geek.base.BaseObserver
import com.xiaomai.geek.base.BaseViewModel
import com.xiaomai.geek.common.PageStatus
import com.xiaomai.geek.db.ArticleRecord
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by wangce on 2018/1/29.
 */
class ArticleViewModel(context: Application) : BaseViewModel(context) {

    private val articleRepository: ArticleRepository = ArticleRepository(ArticleLocalDataSource(getApplication()), ArticleRemoteDataSource())

    fun loadArticles(): Observable<ArticleResponse> {
        return articleRepository.getArticleResponse()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { value ->
                    pageStatus.value = if (value.category.isEmpty()) PageStatus.EMPTY else PageStatus.NORMAL
                }
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

    override fun onCleared() {
        super.onCleared()
        articleRepository.onDestroy()
    }
}