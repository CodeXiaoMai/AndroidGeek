package com.xiaomai.geek.ui.module.articel;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.webkit.WebView;

import com.xiaomai.geek.data.pref.ArticlePref;
import com.xiaomai.geek.ui.widget.MyWebView;

/**
 * Created by XiaoMai on 2017/5/23.
 */

public class ArticleDetailFragment extends WebViewFragment {

    private String mArticleUrl;

    private int mReadProgress;

    public static ArticleDetailFragment newInstance(String url) {
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_URL, url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initCustomWebView(MyWebView webView) {
        super.initCustomWebView(webView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            webView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    mReadProgress = scrollY;
                }
            });
        } else {
            webView.setOnScrollChangedListener(new MyWebView.OnScrollChangedListener() {
                @Override
                public void onScrollChange(int l, int t, int oldl, int oldt) {
                    mReadProgress = t;
                }
            });
        }
    }

    @Override
    protected WebViewFragment getFragmentInstance(String url) {
        return ArticleDetailFragment.newInstance(url);
    }

    @Override
    protected void onLoadFinish(final WebView view, String url) {
        super.onLoadFinish(view, url);
        mArticleUrl = url;
        int readProgress = ArticlePref.getReadProgress(getContext(), url);
        if (readProgress > 100) {
            view.scrollTo(0, readProgress);
            // 使用 SnackBar 根布局要使用 CoordinatorLayout
            Snackbar.make(view, "已跳转到上次阅读的位置", Snackbar.LENGTH_INDEFINITE)
                    .setAction("返回顶部", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            view.scrollTo(0, 0);
                        }
                    }).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        ArticlePref.saveReadProgress(getContext(), mArticleUrl, mReadProgress);
    }
}
