package com.xiaomai.geek.article.viewmodel

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.xiaomai.geek.article.model.*
import com.xiaomai.geek.base.BaseViewModel
import com.xiaomai.geek.base.BaseViewModelObserver
import com.xiaomai.geek.common.PageStatus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by wangce on 2018/1/29.
 */
class ArticleViewModel(context: Application) : BaseViewModel(context) {

    private var articleRepository: ArticleRepository = ArticleRepository(ArticleLocalDataSource(getApplication()), ArticleRemoteDataSource())

    private var articleResponse: MutableLiveData<List<Category>> = MutableLiveData()

    fun getArticles() = articleResponse

    fun loadArticles() {
        pageStatus.value = PageStatus.LOADING
        articleRepository.getArticleResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseViewModelObserver<ArticleResponse>(this@ArticleViewModel) {
                    override fun onSuccess(value: ArticleResponse) {
                        articleResponse.value = value.category
                        pageStatus.value = if (value.category.isEmpty()) PageStatus.EMPTY else PageStatus.NORMAL
                    }
                })
    }

    override fun onCleared() {
        super.onCleared()
        articleRepository.onDestroy()
    }
}