package com.xiaomai.geek.ui.module.articel;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.webkit.WebView;

import com.xiaomai.geek.data.module.Article;
import com.xiaomai.geek.data.pref.ArticlePref;
import com.xiaomai.geek.ui.widget.MyWebView;

/**
 * Created by XiaoMai on 2017/5/16.
 */

public class ArticleDetailActivity extends WebViewActivity {

    public static final String EXTRA_ARTICLE = "extra_article";
    private int mReadProgress;
    private String mArticleUrl;

    public static void launch(Context context, Article article) {
        Intent intent = new Intent(context, ArticleDetailActivity.class);
        intent.putExtra(EXTRA_ARTICLE, article);
        context.startActivity(intent);
    }

    @Override
    protected void loadData() {
        Intent intent = getIntent();
        Article article = intent.getParcelableExtra(EXTRA_ARTICLE);
        mTitle = article.getName();
        toolBar.setTitle(mTitle);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mUrl = article.getUrl();
        mWebView.loadUrl(mUrl);
    }

    @Override
    protected void initCustomWebView(MyWebView webView) {
        super.initCustomWebView(webView);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                mReadProgress = scrollY;
            }
        });
    }
    @Override
    protected void onLoadFinish(final WebView view, String url) {
        super.onLoadFinish(view, url);
        mArticleUrl = url;
        mReadProgress = ArticlePref.getReadProgress(this, url);
        final int progress = mReadProgress;
        if (progress > 100) {
            // 使用 SnackBar 根布局要使用 CoordinatorLayout
            Snackbar.make(view, "是否跳转到上次阅读的位置", Snackbar.LENGTH_INDEFINITE)
                    .setAction("点击跳转", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            nestedScrollView.scrollTo(0, progress);
                        }
                    }).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        ArticlePref.saveReadProgress(this, mArticleUrl, mReadProgress);
    }
}
