package com.xiaomai.geek.ui.module.articel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.orhanobut.logger.Logger;
import com.xiaomai.geek.R;
import com.xiaomai.geek.ui.base.BaseLoadFragment;

/**
 * Created by XiaoMai on 2017/5/22.
 */

public class WebViewFragment extends BaseLoadFragment {

    public static final String EXTRA_URL = "extra_url";

    public static WebViewFragment newInstance(String url) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_URL, url);
        fragment.setArguments(bundle);
        return fragment;
    }

    private WebView mWebView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mWebView = new WebView(getContext());
        initWebViewSettings();
        initWebViewClient();
        initWebView();
        Bundle arguments = getArguments();
        String url = arguments.getString(EXTRA_URL);
        mWebView.loadUrl(url);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mWebView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    Logger.e("onScrollChange");
                }
            });
        }
        return mWebView;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

    }

    private void initWebView() {

    }

    protected void initWebViewClient() {
        WebViewClient webViewClient = new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                /**
                 * 当 WebView 将要加载一个新的 Url 之前，这个方法可以控制 WebView 的行为。
                 * 这里就是拦截了 WebView 将要加载的 Url，即不让当前的 WebView 去加载，而是
                 * 创建一个新的 Fragment 去加载这个 Url。
                 *
                 * 这样解决了WebView 回退时，刷新的问题
                 */
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.hide(WebViewFragment.this);
                transaction.add(R.id.fl_container, WebViewFragment.newInstance(url), String.valueOf(WebViewFragment.this.hashCode()));
                transaction.addToBackStack(null);
                transaction.commit();
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showLoading();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dismissLoading();
                if (!mWebView.getSettings().getLoadsImagesAutomatically()) {
                    mWebView.getSettings().setLoadsImagesAutomatically(true);
                }
            }

        };
        // 如果用户设置了WebViewClient，则在点击新的链接以后就不会跳转到系统浏览器了，而是在本WebView中显示。
        mWebView.setWebViewClient(webViewClient);
    }

    @SuppressLint("SetJavaScriptEnabled")
    protected void initWebViewSettings() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        // WebView中含有可以下载文件的链接，点击该链接后，应该开始执行下载的操作并保存文件到本地中。
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            settings.setLoadsImagesAutomatically(true);
        } else {
            settings.setLoadsImagesAutomatically(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWebView.removeAllViews();
        mWebView.destroy();
    }
}
