package com.xiaomai.geek.article.view

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.xiaomai.geek.article.viewmodel.ArticleViewModel
import com.xiaomai.geek.base.BaseAdapter
import com.xiaomai.geek.base.BaseListActivity
import com.xiaomai.geek.databinding.ArticleCategoryItemBinding
import com.xiaomai.geek.db.ArticleCategory

/**
 * Created by wangce on 2018/1/29.
 */
class ArticleCategoryListActivity : BaseListActivity<ArticleCategory, ArticleCategoryItemBinding, ArticleViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getArticles().observe(this@ArticleCategoryListActivity, Observer {
            mAdapter?.values = it
        })
    }

    override fun getViewModelClazz(): Class<ArticleViewModel> {
        return ArticleViewModel::class.java
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager = GridLayoutManager(this@ArticleCategoryListActivity, 2)

    override fun getAdapter(): BaseAdapter<ArticleCategory, ArticleCategoryItemBinding> = CategoryAdapter()

    override fun loadList() {
        viewModel.loadArticles()
    }
}