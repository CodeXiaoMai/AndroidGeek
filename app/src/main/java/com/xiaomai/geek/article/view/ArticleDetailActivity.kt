package com.xiaomai.geek.article.view

import android.os.Bundle
import com.xiaomai.geek.R
import com.xiaomai.geek.article.model.Article
import com.xiaomai.geek.article.viewmodel.ArticleViewModel
import com.xiaomai.geek.base.BaseViewModelActivity
import com.xiaomai.geek.common.Const
import kotlinx.android.synthetic.main.geek_base_activity.*

/**
 * Created by xiaomai on 2018/2/4.
 */
class ArticleDetailActivity : BaseViewModelActivity<ArticleViewModel>() {

    lateinit var article: Article

    override fun getLayoutId(): Int = R.layout.article_detail_activity

    override fun getViewModelClazz(): Class<ArticleViewModel> = ArticleViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        article = intent.getSerializableExtra(Const.ARTICLE) as Article
        title_view.setTitle(article.name)

        viewModel.saveArticleRecord(article, System.currentTimeMillis(), 0f)
    }
}