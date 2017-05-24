package com.xiaomai.geek.ui.widget;

import android.content.Context;
import android.webkit.WebView;

/**
 * Created by XiaoMai on 2017/5/23.
 */

public class MyWebView extends WebView {

    private OnScrollChangedListener mOnScrollChangedListener;

    public MyWebView(Context context) {
        super(context);
        setHorizontalScrollBarEnabled(false);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangedListener != null) {
            mOnScrollChangedListener.onScrollChange(l, t, oldl, oldt);
        }
    }

    public void setOnScrollChangedListener(OnScrollChangedListener listener) {
        mOnScrollChangedListener = listener;
    }

    public interface OnScrollChangedListener {
        void onScrollChange(int l, int t, int oldl, int oldt);
    }
}
