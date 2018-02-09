package com.xiaomai.geek.article.view

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.webkit.WebView
import android.widget.LinearLayout
import com.just.agentweb.AgentWeb
import com.xiaomai.geek.R
import com.xiaomai.geek.article.model.Article
import com.xiaomai.geek.article.viewmodel.ArticleViewModel
import com.xiaomai.geek.base.BaseObserver
import com.xiaomai.geek.base.BaseViewModelActivity
import com.xiaomai.geek.common.Const
import com.xiaomai.geek.db.ArticleRecord
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.geek_base_activity.*

/**
 * Created by xiaomai on 2018/2/4.
 */
class ArticleDetailActivity : BaseViewModelActivity<ArticleViewModel>() {

    private lateinit var article: Article

    private lateinit var agentWeb: AgentWeb

    private lateinit var webView: WebView

    override fun getLayoutId(): Int = 0

    override fun getViewModelClazz(): Class<ArticleViewModel> = ArticleViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        swipe_refresh_layout.isEnabled = false
        article = intent.getSerializableExtra(Const.ARTICLE) as Article
        title_view.setTitle(article.name)

        agentWeb = AgentWeb.with(this@ArticleDetailActivity)
                .setAgentWebParent(content_view, LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator()
                .setIndicatorColor(R.color.colorAccent)
                .createAgentWeb()
                .ready()
                .go(article.url)

        webView = agentWeb.webCreator.get()

        viewModel.loadArticleRecord(article)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseObserver<ArticleRecord>() {
                    override fun onSuccess(value: ArticleRecord) {
                        webView.scrollY = value.progress.toInt()
                    }
                })

        viewModel.snackbarMessage.observe(this@ArticleDetailActivity, Observer {
            it?.apply {
                Snackbar.make(content_view, this, Snackbar.LENGTH_INDEFINITE)
                        .setAction("返回顶部", {
                            webView.scrollY = 0
                        }).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        agentWeb.webLifeCycle.onResume()
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveArticleRecord(article, System.currentTimeMillis(), webView.scrollY.toFloat())
        agentWeb.webLifeCycle.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        agentWeb.webLifeCycle.onDestroy()
    }

    override fun onBackPressed() {
        if (agentWeb.back()) {
            return
        }
        super.onBackPressed()
    }
}