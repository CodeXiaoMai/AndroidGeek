package com.xiaomai.geek.article.viewmodel

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.xiaomai.geek.article.model.ArticleLocalDataSource
import com.xiaomai.geek.article.model.ArticleRemoteDataSource
import com.xiaomai.geek.article.model.ArticleRepository
import com.xiaomai.geek.article.model.CategoryResponse
import com.xiaomai.geek.base.BaseViewModel
import com.xiaomai.geek.base.BaseViewModelObserver
import com.xiaomai.geek.common.PageStatus
import com.xiaomai.geek.db.ArticleCategory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by wangce on 2018/1/29.
 */
class ArticleViewModel(context: Application) : BaseViewModel(context) {

    private var articleRepository: ArticleRepository = ArticleRepository(ArticleLocalDataSource(getApplication()), ArticleRemoteDataSource())

    private var articleResponse: MutableLiveData<List<ArticleCategory>> = MutableLiveData()

    fun getArticles() = articleResponse

    fun loadArticles() {
        pageStatus.value = PageStatus.LOADING
        articleRepository.getArticleCategories()
                .subscribeOn(Schedulers.io())
                .doOnNext { value ->
//                    articleRepository.saveArticleCategories(value)
                    TODO("只保存阅读过的文章")
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseViewModelObserver<CategoryResponse>(this@ArticleViewModel) {
                    override fun onSuccess(value: CategoryResponse) {
                        articleResponse.value = value.list
                        pageStatus.value = if (value.list.isEmpty()) PageStatus.EMPTY else PageStatus.NORMAL
                    }
                })
    }

    override fun onCleared() {
        super.onCleared()
        articleRepository.onDestroy()
    }
}