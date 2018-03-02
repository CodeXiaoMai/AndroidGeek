package com.xiaomai.geek.model.article.view

import android.os.Bundle
import com.xiaomai.geek.base.BaseAdapter
import com.xiaomai.geek.base.BaseListActivity
import com.xiaomai.geek.common.Const
import com.xiaomai.geek.common.NullViewModel
import com.xiaomai.geek.databinding.ArticleItemBinding
import com.xiaomai.geek.model.article.model.Article
import com.xiaomai.geek.model.article.model.Category
import kotlinx.android.synthetic.main.geek_base_activity.*

/**
 * Created by xiaomai on 2018/2/2.
 */
class ArticleListActivity : BaseListActivity<Article, ArticleItemBinding, NullViewModel>() {

    var category: Category? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        category = intent.getSerializableExtra(Const.ARTICLE) as Category

        super.onCreate(savedInstanceState)
        swipe_refresh_layout.isEnabled = false

        title_view.setTitle(category?.name ?: "文章列表")
    }

    override fun loadList() {
        if (category == null || category?.articles == null) {
            showEmpty()
        }
        category?.articles?.let {
            mAdapter.values = it
            if (it.isEmpty()) showEmpty() else showContent()
        }
    }

    override fun getAdapter(): BaseAdapter<Article, ArticleItemBinding> = ArticleAdapter()

    override fun getViewModelClazz(): Class<NullViewModel> = NullViewModel::class.java
}