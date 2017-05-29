package com.xiaomai.geek.ui.module.articel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.xiaomai.geek.R;
import com.xiaomai.geek.common.utils.NetworkUtil;
import com.xiaomai.geek.common.utils.ShareUtils;
import com.xiaomai.geek.ui.base.BaseLoadActivity;
import com.xiaomai.geek.ui.widget.MyWebView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XiaoMai on 2017/5/16.
 */

public class WebViewActivity extends BaseLoadActivity {

    public static final String EXTRA_URL = "extra_url";

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;

    protected MyWebView mWebView;

    protected String mTitle;
    protected String mUrl;

    public static void launch(Context context, String url, String title) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(Intent.EXTRA_TITLE, title);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        initViews();
        loadData();
    }

    protected void loadData() {
        Intent intent = getIntent();
        if (null == intent)
            return;
        mTitle = intent.getStringExtra(Intent.EXTRA_TITLE);
        toolBar.setTitle(mTitle);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUrl = intent.getStringExtra(EXTRA_URL);
        if (TextUtils.isEmpty(mUrl))
            return;
        mWebView.loadUrl(mUrl);
    }

    protected void initViews() {
        mWebView = new MyWebView(this);
        flContainer.addView(mWebView);
        initCustomWebView(mWebView);
        initWebViewSettings();
        initWebViewClient();
    }

    protected void initCustomWebView(MyWebView webView) {
    }

    @SuppressLint("SetJavaScriptEnabled")
    protected void initWebViewSettings() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        if (NetworkUtil.isNetworkAvailable(this)) {
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        // WebView中含有可以下载文件的链接，点击该链接后，应该开始执行下载的操作并保存文件到本地中。
        // 重写 shouldOverrideUrlLoading 方法后总是 Crash
//        mWebView.setDownloadListener(new DownloadListener() {
//
//            @Override
//            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
//                Uri uri = Uri.parse(url);
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
//            }
//        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            settings.setLoadsImagesAutomatically(true);
        } else {
            settings.setLoadsImagesAutomatically(false);
        }
    }

    protected void initWebViewClient() {
        WebViewClient webViewClient = new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                /**
                 * 当 MyWebView 将要加载一个新的 Url 之前，这个方法可以控制 MyWebView 的行为。
                 * 这里就是拦截了 MyWebView 将要加载的 Url，即不让当前的 MyWebView 去加载，而是
                 * 创建一个新的 Fragment 去加载这个 Url。
                 *
                 * 这样解决了WebView 回退时，刷新的问题
                 */
                WebViewActivity.launch(WebViewActivity.this, url, url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                onPageStart(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                onLoadFinish(view, url);
            }
        };
        // 如果用户设置了WebViewClient，则在点击新的链接以后就不会跳转到系统浏览器了，而是在本WebView中显示。
        mWebView.setWebViewClient(webViewClient);

        WebChromeClient webChromeClient = new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress >= 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setProgress(newProgress);
                    progressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                setTitle(title);
            }
        };
        mWebView.setWebChromeClient(webChromeClient);
    }

    protected void onPageStart(WebView view, String url, Bitmap favicon) {

    }

    protected void onLoadFinish(WebView view, String url) {
        if (!mWebView.getSettings().getLoadsImagesAutomatically()) {
            mWebView.getSettings().setLoadsImagesAutomatically(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_share:
                if (!TextUtils.isEmpty(mUrl)) {
                    ShareUtils.share(WebViewActivity.this, mTitle, mUrl);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flContainer.removeAllViews();
        mWebView.removeAllViews();
        mWebView.destroy();
    }
}
