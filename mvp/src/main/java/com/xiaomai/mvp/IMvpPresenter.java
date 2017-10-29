package com.xiaomai.mvp;

import android.support.annotation.UiThread;

/**
 * Created by xiaomai on 2017/10/25.
 */

public interface IMvpPresenter<V extends IMvpView> {

    @UiThread
    void attachView(V view);

    @UiThread
    void detachView();
}
