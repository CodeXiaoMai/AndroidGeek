package com.xiaomai.geek.article.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import com.xiaomai.geek.R
import com.xiaomai.geek.article.viewmodel.ArticleViewModel
import com.xiaomai.geek.base.BaseActivity
import kotlinx.android.synthetic.main.article_list_activity.*

/**
 * Created by wangce on 2018/1/29.
 */
class ArticleListActivity : BaseActivity() {

    private lateinit var viewModel: ArticleViewModel

    private lateinit var adapter: ArticleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.article_list_activity)

        recycler_view.layoutManager = GridLayoutManager(this@ArticleListActivity, 2)
        adapter = ArticleAdapter()
        recycler_view.adapter = adapter

        viewModel = ViewModelProviders.of(this@ArticleListActivity).get(ArticleViewModel::class.java)
        viewModel.getArticles().observe(this@ArticleListActivity, Observer {
            adapter.articles = it
            swipe_refresh_layout.isRefreshing = false
        })

        swipe_refresh_layout.setOnRefreshListener { loadArticles() }

        swipe_refresh_layout.post {
            swipe_refresh_layout.isRefreshing = true
            loadArticles()
        }
    }

    private fun loadArticles() {
        viewModel.loadArticles()
    }
}