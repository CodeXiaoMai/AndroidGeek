package com.xiaomai.geek.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import com.xiaomai.geek.ui.widget.LoadingView;
import com.xiaomai.mvp.lce.ILoadView;

/**
 * Created by XiaoMai on 2017/4/24.
 */

public abstract class BaseLoadActivity extends BaseActivity implements ILoadView{

    private LoadingView mLoadingView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoadingView = new LoadingView(this, getLoadingMessage());
    }

    @Override
    public void showLoading() {
        mLoadingView.show();
    }

    @Override
    public void dismissLoading() {
        mLoadingView.dismiss();
    }

    @Override
    public void error(Throwable e) {
        Snackbar.make(getWindow().getDecorView(), e.getMessage(), Snackbar.LENGTH_LONG).show();
    }

    public String getLoadingMessage() {
        return "加载中...";
    }

}
