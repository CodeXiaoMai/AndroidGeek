package com.xiaomai.geek.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.xiaomai.geek.presenter.BasePresenter;
import com.xiaomai.mvp.IMvpView;

/**
 * Created by xiaomai on 2017/10/26.
 */

public abstract class BaseFragment<M> extends Fragment implements IMvpView {

    protected Context mContext;

    protected BasePresenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getContext();

        mPresenter = getPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    protected abstract BasePresenter getPresenter();

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
