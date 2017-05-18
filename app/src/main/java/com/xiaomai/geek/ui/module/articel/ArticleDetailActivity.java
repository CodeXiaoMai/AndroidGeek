package com.xiaomai.geek.ui.module.articel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xiaomai.geek.R;
import com.xiaomai.geek.data.module.Article;
import com.xiaomai.geek.data.pref.ArticlePref;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XiaoMai on 2017/5/16.
 */

public class ArticleDetailActivity extends BaseWebViewActivity {

    public static final String EXTRA_ARTICLE = "extra_article";

    @BindView(R.id.tool_bar)
    Toolbar toolBar;

    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;

    private String mArticleUrl;

    private int mReadProgress;

    public static void launch(Context context, Article article) {
        Intent intent = new Intent(context, ArticleDetailActivity.class);
        intent.putExtra(EXTRA_ARTICLE, article);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        ButterKnife.bind(this);
        initViews();
        initWebViewSettings();
        initWebViewClient();
    }

    private void initWebViewClient() {
        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showLoading();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dismissLoading();
                int readProgress = ArticlePref.getReadProgress(ArticleDetailActivity.this, mArticleUrl);
                if (readProgress > 100) {
                    nestedScrollView.smoothScrollTo(0, readProgress);
                    Snackbar.make(nestedScrollView, "已跳转到上次阅读的位置", Snackbar.LENGTH_INDEFINITE)
                            .setAction("返回顶部", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    nestedScrollView.smoothScrollTo(0, 0);
                                }
                            }).show();
                }
            }

        };
        // 如果用户设置了WebViewClient，则在点击新的链接以后就不会跳转到系统浏览器了，而是在本WebView中显示。
        webView.setWebViewClient(webViewClient);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebViewSettings() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // WebView中含有可以下载文件的链接，点击该链接后，应该开始执行下载的操作并保存文件到本地中。
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                if (webView.canGoBack())
                    webView.goBack();
                else
                    finish();
            }
        });
    }

    private float lastX;
    private void initViews() {
        Intent intent = getIntent();
        Article article = intent.getParcelableExtra(EXTRA_ARTICLE);
        toolBar.setTitle(article.getName());
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mArticleUrl = article.getUrl();
        webView.loadUrl(mArticleUrl);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                mReadProgress = scrollY;
            }
        });
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                if (Math.abs(x - lastX) >= 5) {
                    ((WebView) v).requestDisallowInterceptTouchEvent(true);
                } else {
                    ((WebView) v).requestDisallowInterceptTouchEvent(false);
                }
                lastX = x;
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ArticlePref.saveReadProgress(this, mArticleUrl, mReadProgress);
    }
}
