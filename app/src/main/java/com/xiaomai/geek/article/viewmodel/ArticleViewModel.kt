package com.xiaomai.geek.article.viewmodel

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.xiaomai.geek.article.model.ArticleRemoteDataSource
import com.xiaomai.geek.article.model.ArticleRepository
import com.xiaomai.geek.article.model.ArticleResponse
import com.xiaomai.geek.base.BaseAndroidViewModel
import com.xiaomai.geek.base.BaseObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by wangce on 2018/1/29.
 */
class ArticleViewModel(context: Application) : BaseAndroidViewModel(context) {

    private var articleRepository: ArticleRepository? = null

    private var articles: MutableLiveData<List<ArticleResponse>> = MutableLiveData()

    fun getArticles() = articles

    fun loadArticles() {
        if (articleRepository == null) {
            articleRepository = ArticleRepository(ArticleRemoteDataSource())
        }
        articleRepository?.let {
            it.getArticles()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : BaseObserver<List<ArticleResponse>>(compositeDisposable) {
                        override fun onNext(t: List<ArticleResponse>) {
                            articles.value = t
                        }
                    })
        }
    }
}