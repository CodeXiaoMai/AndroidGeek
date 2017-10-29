package com.xiaomai.geek.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

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

        mContext = this;
    }
}
