package com.xiaomai.geek.article.view

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.xiaomai.geek.article.model.ArticleResponse
import com.xiaomai.geek.article.viewmodel.ArticleViewModel
import com.xiaomai.geek.base.BaseAdapter
import com.xiaomai.geek.base.BaseListActivity
import com.xiaomai.geek.databinding.ArticleItemBinding

/**
 * Created by wangce on 2018/1/29.
 */
class ArticleListActivity : BaseListActivity<ArticleResponse, ArticleItemBinding, ArticleViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getArticles().observe(this@ArticleListActivity, Observer {
            mAdapter?.values = it
        })
    }

    override fun getViewModelClazz(): Class<ArticleViewModel> {
        return ArticleViewModel::class.java
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager = GridLayoutManager(this@ArticleListActivity, 2)

    override fun getAdapter(): BaseAdapter<ArticleResponse, ArticleItemBinding> = ArticleAdapter()

    override fun loadList() {
        viewModel.loadArticles()
    }
}