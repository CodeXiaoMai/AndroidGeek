package com.xiaomai.geek.ui.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.xiaomai.geek.R;
import com.xiaomai.geek.common.wrapper.AppLog;
import com.xiaomai.mvp.IMvpView;

/**
 * Created by xiaomai on 2017/10/25.
 */

public abstract class BaseActivity extends AppCompatActivity implements IMvpView {

    protected final String TAG = getClass().getSimpleName();

    protected Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppLog.d(TAG, "=========================== " + TAG + " Start ===============================");
        mContext = this;
        setContentView(getLayoutResource());
        initViews();
    }

    @LayoutRes
    protected int getLayoutResource() {
        return R.layout.activity_common;
    }

    public void initViews() {

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
