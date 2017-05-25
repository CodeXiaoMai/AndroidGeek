package com.xiaomai.geek.ui.module.articel;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.xiaomai.geek.R;
import com.xiaomai.geek.ui.base.BaseLoadFragment;
import com.xiaomai.geek.ui.widget.MyWebView;

/**
 * Created by XiaoMai on 2017/5/22.
 */

public class WebViewFragment extends BaseLoadFragment {

    public static final String EXTRA_URL = "extra_url";
    private RelativeLayout mRootlayout;

    public static WebViewFragment newInstance(String url) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_URL, url);
        fragment.setArguments(bundle);
        return fragment;
    }

    private ProgressBar mProgressBar;
    private MyWebView mWebView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootlayout = new RelativeLayout(getContext());
        mRootlayout.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
        mWebView = new MyWebView(getContext());
        mRootlayout.addView(mWebView);
        mProgressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
        mProgressBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mRootlayout.addView(mProgressBar);
        initWebViewSettings();
        initWebViewClient();
        Bundle arguments = getArguments();
        String url = arguments.getString(EXTRA_URL);
        initCustomWebView(mWebView);
        mWebView.loadUrl(url);
        return mRootlayout;
    }

    protected void initCustomWebView(MyWebView webView) {
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
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.hide(WebViewFragment.this);
                WebViewFragment fragment = getFragmentInstance(url);
                transaction.add(R.id.fl_container, fragment, String.valueOf(fragment.hashCode()));
                transaction.addToBackStack(null);
                transaction.commit();
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
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setProgress(newProgress);
                    mProgressBar.setVisibility(View.VISIBLE);
                }
            }
        };
        mWebView.setWebChromeClient(webChromeClient);
    }

    protected WebViewFragment getFragmentInstance(String url) {
        return WebViewFragment.newInstance(url);
    }

    protected void onPageStart(WebView view, String url, Bitmap favicon) {

    }

    protected void onLoadFinish(WebView view, String url) {
        if (!mWebView.getSettings().getLoadsImagesAutomatically()) {
            mWebView.getSettings().setLoadsImagesAutomatically(true);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    protected void initWebViewSettings() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRootlayout.removeAllViews();
        mWebView.removeAllViews();
        mWebView.destroy();
    }
}
