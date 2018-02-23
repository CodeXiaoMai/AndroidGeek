package com.xiaomai.geek.article.view

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.xiaomai.geek.article.model.ArticleResponse
import com.xiaomai.geek.article.model.Category
import com.xiaomai.geek.article.viewmodel.ArticleViewModel
import com.xiaomai.geek.base.BaseAdapter
import com.xiaomai.geek.base.BaseListFragment
import com.xiaomai.geek.base.BaseViewModelObserver
import com.xiaomai.geek.databinding.ArticleCategoryItemBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by wangce on 2018/1/29.
 */
class ArticleCategoryListFragment : BaseListFragment<Category, ArticleCategoryItemBinding, ArticleViewModel>() {

    companion object {
        fun newInstance() : ArticleCategoryListFragment{
            val fragment = ArticleCategoryListFragment()
            return fragment
        }
    }

    override fun getViewModelClazz(): Class<ArticleViewModel> {
        return ArticleViewModel::class.java
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager = GridLayoutManager(context, 2)

    override fun getAdapter(): BaseAdapter<Category, ArticleCategoryItemBinding> = CategoryAdapter()

    override fun loadList() {
        viewModel.loadArticles()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    showLoading()
                }
                .subscribe(object : BaseViewModelObserver<ArticleResponse>(viewModel) {
                    override fun onSuccess(value: ArticleResponse) {
                        mAdapter?.values = value.category
                    }
                })
    }
}