package com.xiaomai.geek.model.article.view

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.xiaomai.geek.R
import com.xiaomai.geek.base.BaseAdapter
import com.xiaomai.geek.base.BaseListActivity
import com.xiaomai.geek.databinding.ArticleItemBinding
import com.xiaomai.geek.db.Article
import com.xiaomai.geek.model.article.viewmodel.ArticleViewModel
import kotlinx.android.synthetic.main.article_search_activity.*
import kotlinx.android.synthetic.main.geek_base_activity.*

/**
 * Created by wangce on 2018/3/6.
 */
class ArticleSearchActivity : BaseListActivity<Article, ArticleItemBinding, ArticleViewModel>() {

    override fun getLayoutId(): Int = R.layout.article_search_activity

    override fun getViewModelClazz(): Class<ArticleViewModel> = ArticleViewModel::class.java

    override fun loadList() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        swipe_refresh_layout.isEnabled = false
        edit_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.apply {
                    viewModel.searchArticle(s.toString())
                }
            }
        })

        viewModel.searchResult.observe(this, Observer {
            if (it == null) {

            } else {
                mAdapter.values = it.toMutableList()
            }
        })
    }

    override fun showEmpty() {
        article_search_empty.visibility = View.VISIBLE
        recycler_view.visibility = View.GONE
    }

    override fun showContent() {
        article_search_empty.visibility = View.GONE
        recycler_view.visibility = View.VISIBLE
    }

    override fun getAdapter(): BaseAdapter<Article, ArticleItemBinding> = ArticleAdapter()
}