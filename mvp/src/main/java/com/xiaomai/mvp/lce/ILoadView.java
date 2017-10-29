package com.xiaomai.mvp.lce;

import android.support.annotation.UiThread;

import com.xiaomai.mvp.IMvpView;

/**
 * Created by xiaomai on 2017/10/25.
 */

public interface ILoadView extends IMvpView {

    @UiThread
    void showLoading();

    @UiThread
    void dismissLoading();

    @UiThread
    void error(Throwable e);
}