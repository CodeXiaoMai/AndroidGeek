package com.xiaomai.geek.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.xiaomai.geek.presenter.BaseRxPresenter;
import com.xiaomai.mvp.MvpView;

/**
 * Created by XiaoMai on 2017/3/29 18:40.
 */

public abstract class BaseActivity<T extends BaseRxPresenter> extends AppCompatActivity implements MvpView {

    protected Context mContext;

    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    protected void initToolBar(String title) {
        ActionBar actionBar = getSupportActionBar();
        initToolBar(actionBar, true, title);
    }

    protected void initToolBar(ActionBar actionBar, boolean homeAsUpEnabled, String title){
        if (actionBar != null) {
        }
    }
}
