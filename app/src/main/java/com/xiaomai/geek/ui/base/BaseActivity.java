package com.xiaomai.geek.ui.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.xiaomai.geek.R;
import com.xiaomai.geek.common.wrapper.AppLog;
import com.xiaomai.geek.ui.widget.TitleView;
import com.xiaomai.mvp.IMvpView;

/**
 * Created by xiaomai on 2017/10/25.
 */

public abstract class BaseActivity extends AppCompatActivity implements IMvpView {

    protected final String TAG = getClass().getSimpleName();
    private static final int DEFAULT_LAYOUT = R.layout.activity_common;

    protected Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppLog.d(TAG, "=========================== " + TAG + " Start ===============================");
        mContext = this;
        setContentView(getLayoutResource());
        if (DEFAULT_LAYOUT == getLayoutResource()) {
            FrameLayout containerView = findViewById(R.id.container_view);
            setContainerView(containerView);
        }
        beforeInitViews();
        initViews();
        afterInitViews();
        loadData();
        afterLoadData();
    }

    @Override
    public void setTitle(int titleId) {
        setTitle(getString(titleId));
    }

    @Override
    public void setTitle(CharSequence title) {
        TitleView titleView = findViewById(R.id.title_view);
        titleView.setTitle(title.toString());
    }

    protected void setContainerView(FrameLayout containerView) {
    }

    protected void afterLoadData() {
    }

    protected void afterInitViews() {
    }

    protected void beforeInitViews() {
    }

    protected void loadData() {
    }

    @LayoutRes
    protected int getLayoutResource() {
        return DEFAULT_LAYOUT;
    }

    protected void initViews() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppLog.d(TAG, "=========================== " + TAG + " Finish ==============================");
    }

    protected String getStringFromIntent(String key) {
        Intent intent = getIntent();
        if (intent == null) {
            return null;
        }
        return intent.getStringExtra(key);
    }

    protected Parcelable getParcelableFromIntent(String key) {
        Intent intent = getIntent();
        if (intent == null) {
            return null;
        }
        return intent.getParcelableExtra(key);
    }
}
